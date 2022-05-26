@Library('surf-lib@version-4.1.1-SNAPSHOT')
// https://github.com/surfstudio/jenkins-pipeline-lib

import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.JarvisUtil
import ru.surfstudio.ci.CommonUtil
import ru.surfstudio.ci.pipeline.ScmPipeline
import ru.surfstudio.ci.RepositoryUtil
import ru.surfstudio.ci.Result
import ru.surfstudio.ci.AbortDuplicateStrategy

// !!! Релиз артефактов в maven central. Активно не используется.
// Соответствует job Standard-Release_Android_TAG в jenkins

// Stage names

def CHECKOUT = 'Checkout'
def SET_COMPONENT_ALPHA_COUNTER_TO_ZERO = "Set Component Alpha Counter To Zero"

def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def DEPLOY_MODULES = 'Deploy Modules'
def COMPONENT_ALPHA_COUNTER_PUSH = 'Component Alpha Counter Push'

//vars
def branchName = ""
def componentVersion = "<unknown>"
def componentName = "<unknown>"
def buildDescription = ""

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
        pipeline.stage(SET_COMPONENT_ALPHA_COUNTER_TO_ZERO) {
            script.sh("./gradlew setComponentAlphaCounterToZero -Pcomponent=${componentName}")
        },
        pipeline.stage(BUILD) {
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assembleRelease")
        },
        pipeline.stage(UNIT_TEST) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(
                    script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/"
            )
        },
        pipeline.stage(DEPLOY_MODULES) {
            withJobCredentials(script) {
                def publishTask = "./gradlew clean publish -Pcomponent=$componentName"
                // --no-parallel to avoid possible bugs with deploy to different staging repositories
                // -Dhttp.connectionTimeout=60000 to avoid possible Read timed out errors during deploy
                script.sh "$publishTask -PpublishType=maven_release --no-parallel -Dhttp.connectionTimeout=60000"
                script.sh "$publishTask -PpublishType=artifactory"
            }
        },
        pipeline.stage(COMPONENT_ALPHA_COUNTER_PUSH, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            RepositoryUtil.setDefaultJenkinsGitUser(script)
            def labels = "$RepositoryUtil.SKIP_CI_LABEL1 $RepositoryUtil.VERSION_LABEL1"
            def tag = "$componentName/$componentVersion"
            script.sh "git commit -a -m " +
                    "\"Set component $componentName alpha counter to zero $labels\" || true"
            script.sh "git tag -a $tag -m \"Set tag $tag $labels\""
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
            initBuildDiscarder(script),
            initParameters(script)
    ]
}

def static initBuildDiscarder(script) {
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

// ============================================= ↑↑↑  END JOB PROPERTIES CONFIGURATION ↑↑↑  ==========================================

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
                    credentialsId: "Maven_Deploy_Credentials",
                    usernameVariable: 'surf_ossrh_username',
                    passwordVariable: 'surf_ossrh_password'
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