@Library('surf-lib@version-4.1.3-SNAPSHOT')
// https://github.com/surfstudio/jenkins-pipeline-lib
import groovy.json.JsonSlurperClassic
import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.ScmPipeline
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.utils.buildsystems.GradleUtil

//Pipeline for deploy snapshot artifacts

// !!! Job оставлен для обратной совместимости.
// Актуальная версия переписана на Github Actions, см .github/workflows/main-deploy.yml
// При необходимости можно включить job Standard-Deploy-Dev_Android_TAG в jenkins

// Stage names
def CHECKOUT = 'Checkout'
def INCREMENT_GLOBAL_ALPHA_VERSION = 'Increment Global Alpha Version'
def BUILD = 'Build'
def BUILD_TEMPLATE = 'Template Build'
def UNIT_TEST = 'Unit Test'
def UPDATE_TEMPLATE_VERSION_PLUGIN = 'Update Template Version Plugin'
def DEPLOY_MODULES = 'Deploy Modules'
def DEPLOY_GLOBAL_VERSION_PLUGIN = 'Deploy Global Version Plugin'
def VERSION_PUSH = 'Version Push'

//constants
def projectConfigurationFile = "buildSrc/projectConfiguration.json"

//vars
def branchName = ""
def buildDescription = ""
def useJava11 = true

//init
def script = this
def pipeline = new EmptyScmPipeline(script)

pipeline.init()

//configuration
pipeline.node = "android"
pipeline.propertiesProvider = { initProperties(pipeline) }

pipeline.preExecuteStageBody = { stage ->
    if (stage.name != CHECKOUT) RepositoryUtil.notifyGithubAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    if (stage.name != CHECKOUT) RepositoryUtil.notifyGithubAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
}

pipeline.initializeBody = {
    CommonUtil.printInitialStageStrategies(pipeline)

    //Выбираем значения веток из параметров, Установка их в параметры происходит
    // если триггером был webhook или если стартанули Job вручную
    //Используется имя branchName_0 из за особенностей jsonPath в GenericWebhook plugin
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'branchName') {
        value -> branchName = value
    }

    branchName = branchName.replace("refs/heads/", "")
    if (branchName.contains("origin/")) {
        branchName = branchName.replace("origin/", "")
    }

    buildDescription = branchName
    CommonUtil.setBuildDescription(script, buildDescription)
}

pipeline.stages = [
        pipeline.stage(CHECKOUT) {
            script.git(
                    url: pipeline.repoUrl,
                    credentialsId: pipeline.repoCredentialsId
            )
            script.sh "git checkout -B $branchName origin/$branchName"

            script.echo "Checking $RepositoryUtil.SKIP_CI_LABEL1 label in last commit message for automatic builds"
            if (RepositoryUtil.isCurrentCommitMessageContainsSkipCiLabel(script) && !CommonUtil.isJobStartedByUser(script)) {
                scmSkip(deleteBuild: true, skipPattern: '.*\\[skip ci\\].*')
            }
            CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)

            RepositoryUtil.saveCurrentGitCommitHash(script)
            RepositoryUtil.checkLastCommitMessageContainsSkipCiLabel(script)
        },
        pipeline.stage(INCREMENT_GLOBAL_ALPHA_VERSION) {
            GradleUtil.gradlew(script, "incrementGlobalUnstableVersion", useJava11)
        },
        pipeline.stage(UPDATE_TEMPLATE_VERSION_PLUGIN) {
            GradleUtil.gradlew(script, "incrementTemplateVersion", useJava11)
        },
        pipeline.stage(BUILD) {
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assembleRelease", useJava11)
        },
        pipeline.stage(UNIT_TEST) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(
                    script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/",
                    useJava11
            )
        },
        pipeline.stage(DEPLOY_MODULES) {
            withJobCredentials(script) {
                GradleUtil.gradlew(script, "clean publish -PpublishType=artifactory", useJava11)
            }
        },
        pipeline.stage(DEPLOY_GLOBAL_VERSION_PLUGIN) {
            withJobCredentials(script) {
                GradleUtil.gradlew(script, "generateDataForPlugin", useJava11)
                GradleUtil.gradlew(script, ":android-standard-version-plugin:publish", useJava11)
            }
        },
        pipeline.stage(BUILD_TEMPLATE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("echo \"androidStandardDebugDir=$workspace\n" +
                    "androidStandardDebugMode=false\" > template/android-standard/androidStandard.properties")
            // build template after deploy in order to check usage of new artifacts
            GradleUtil.gradlew(script, "-p template :app:dependencies", useJava11)
            AndroidPipelineHelper.buildStageBodyAndroid(script, "-p template clean assembleQa assembleRelease --stacktrace", useJava11)
        },
        pipeline.stage(VERSION_PUSH, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            RepositoryUtil.setDefaultJenkinsGitUser(script)
            def globalConfiguration = getGlobalConfiguration(script, projectConfigurationFile)

            script.sh "git commit -a -m \"Increase global alpha version counter to " +
                    "$globalConfiguration.unstable_version $RepositoryUtil.SKIP_CI_LABEL1 $RepositoryUtil.VERSION_LABEL1\""
            RepositoryUtil.push(script, pipeline.repoUrl, pipeline.repoCredentialsId)
        }
]


pipeline.finalizeBody = {
    def jenkinsLink = CommonUtil.getBuildUrlSlackLink(script)
    def message
    def success = Result.SUCCESS == pipeline.jobResult
    def unstable = Result.UNSTABLE == pipeline.jobResult
    def checkoutAborted = pipeline.getStage(CHECKOUT).result == Result.ABORTED
    if (!checkoutAborted) {
        if (!success) {
            def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
            if (unstable) {
                message = "Deploy из ветки '${branchName}' выполнен. Найдены нестабильные этапы: ${unsuccessReasons}. ${jenkinsLink}"
            } else {
                message = "Deploy из ветки '${branchName}' не выполнен из-за этапов: ${unsuccessReasons}. ${jenkinsLink}"
            }
        } else {
            message = "Deploy из ветки '${branchName}' успешно выполнен. ${jenkinsLink}"
        }

        JarvisUtil.sendMessageToGroup(script, message, pipeline.repoUrl, "github", pipeline.jobResult)
    }
}

pipeline.run()

// ============================================= ↓↓↓ JOB PROPERTIES CONFIGURATION ↓↓↓  ==========================================

static List<Object> initProperties(ScmPipeline ctx) {
    def script = ctx.script
    return [
            initDiscarder(script),
            initParameters(script),
            initTriggers(script)
    ]
}

def static initDiscarder(script) {
    return script.buildDiscarder(
            script.logRotator(
                    artifactDaysToKeepStr: '3',
                    artifactNumToKeepStr: '10',
                    daysToKeepStr: '60',
                    numToKeepStr: '200'
            )
    )
}

def static initParameters(script) {
    return script.parameters([
            script.string(
                    name: "branchName",
                    description: 'Ветка с исходным кодом'
            )
    ])
}

def static initTriggers(script) {
    return script.pipelineTriggers([
            script.GenericTrigger(
                    genericVariables: [
                            [
                                    key  : "branchName",
                                    value: '$.ref'
                            ]
                    ],
                    printContributedVariables: true,
                    printPostContent: true,
                    causeString: 'Triggered by Github',
                    regexpFilterExpression: '^(origin\\/)?refs\\/heads\\/dev\\/G-(.*)$',
                    regexpFilterText: '$branchName'
            ),
            script.pollSCM('')
    ])
}

// ============================================== ↑↑↑  END JOB PROPERTIES CONFIGURATION ↑↑↑  ==========================================

// ============ Utils =================

def static withJobCredentials(script, body) {
    script.withCredentials([
            script.file(
                    credentialsId: "surf_maven_sign_key_ring_file",
                    variable: 'surf_maven_sign_key_ring_file'
            ),
            script.usernamePassword(
                    credentialsId: "Maven_Sign_Credential",
                    usernameVariable: 'surf_maven_sign_key_id',
                    passwordVariable: 'surf_maven_sign_password'
            ),
            script.usernamePassword(
                    credentialsId: "Artifactory_Deploy_Credentials",
                    usernameVariable: 'surf_maven_username',
                    passwordVariable: 'surf_maven_password'
            )
    ]) {
        body()
    }
}

def static getGlobalConfiguration(script, projectConfigurationFile) {
    String globalConfigurationJsonStr = script.readFile(projectConfigurationFile)
    return new JsonSlurperClassic().parseText(globalConfigurationJsonStr)
}
