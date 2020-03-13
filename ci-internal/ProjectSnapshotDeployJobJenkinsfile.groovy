@Library('surf-lib@version-2.0.0-SNAPSHOT')
// https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import groovy.json.JsonSlurperClassic
import ru.surfstudio.ci.pipeline.ScmPipeline
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.JarvisUtil
import ru.surfstudio.ci.CommonUtil
import ru.surfstudio.ci.RepositoryUtil
import ru.surfstudio.ci.utils.android.AndroidUtil
import ru.surfstudio.ci.Result
import ru.surfstudio.ci.AbortDuplicateStrategy
import ru.surfstudio.ci.utils.android.config.AndroidTestConfig
import ru.surfstudio.ci.utils.android.config.AvdConfig

//Pipeline for deploy project snapshot artifacts

// Stage names

def CHECKOUT = 'Checkout'
def CHECK_BRANCH_AND_VERSION = 'Check Branch & Version'
def CHECK_CONFIGURATION_IS_PROJECT_SNAPHOT = 'Check Configuration is project snapshot'
def INCREMENT_PROJECT_SNAPSHOT_VERSION = 'Increment Project Snapshot Version'
def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'
def DEPLOY_MODULES = 'Deploy Modules'
def DEPLOY_GLOBAL_VERSION_PLUGIN = 'Deploy Global Version Plugin'
def VERSION_PUSH = 'Version Push'

//constants
def projectConfigurationFile = "buildSrc/projectConfiguration.json"

//vars
def branchName = ""
def useBintrayDeploy = false
def skipIncrementVersion = false

//other config

def getTestInstrumentationRunnerName = { script, prefix ->
    def defaultInstrumentationRunnerGradleTaskName = "printTestInstrumentationRunnerName"
    return script.sh(
            returnStdout: true,
            script: "./gradlew :$prefix:$defaultInstrumentationRunnerGradleTaskName | tail -4 | head -1"
    )
}

//init
def script = this
def pipeline = new EmptyScmPipeline(script)

pipeline.init()

//configuration
pipeline.node = "android"
pipeline.propertiesProvider = { initProperties(pipeline) }

pipeline.preExecuteStageBody = { stage ->
    if (stage.name != CHECKOUT) RepositoryUtil.notifyBitbucketAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    if (stage.name != CHECKOUT) RepositoryUtil.notifyBitbucketAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
}

pipeline.initializeBody = {
    CommonUtil.printInitialStageStrategies(pipeline)

    script.echo "artifactory user: ${script.env.surf_maven_username}"

    //Выбираем значения веток из параметров, Установка их в параметры происходит
    // если триггером был webhook или если стартанули Job вручную
    //Используется имя branchName_0 из за особенностей jsonPath в GenericWebhook plugin
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'branchName_0') {
        value -> branchName = value
    }
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'useBintrayDeploy') {
        value -> useBintrayDeploy = value
    }
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'skipIncrementVersion') {
        value -> skipIncrementVersion = value
    }

    if (branchName.contains("origin/")) {
        branchName = branchName.replace("origin/", "")
    }

    def buildDescription = branchName
    CommonUtil.setBuildDescription(script, buildDescription)
    CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)
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
                throw new InterruptedException("Job aborted, because it triggered automatically and last commit message contains $RepositoryUtil.SKIP_CI_LABEL1 label")
            }

            RepositoryUtil.saveCurrentGitCommitHash(script)
        },
        pipeline.stage(CHECK_BRANCH_AND_VERSION, StageStrategy.SKIP_STAGE) {
            String globalConfigurationJsonStr = script.readFile(projectConfigurationFile)
            def globalConfiguration = new JsonSlurperClassic().parseText(globalConfigurationJsonStr)
            project = globalConfiguration.project_snapshot_name

            if (("project-snapshot/" + project) != branchName) {
                script.error("Deploy AndroidStandard for project: $project from branch: '$branchName' forbidden")
            }
        },
        pipeline.stage(CHECK_CONFIGURATION_IS_PROJECT_SNAPHOT, StageStrategy.SKIP_STAGE) {
            script.sh("./gradlew checkConfigurationIsProjectSnapshotTask")
        },
        pipeline.stage(INCREMENT_PROJECT_SNAPSHOT_VERSION, StageStrategy.SKIP_STAGE) {
            if (!skipIncrementVersion) {
                script.sh("./gradlew incrementProjectSnapshotVersion")
            } else {
                script.echo "skip project snapshot version incrementation stage"
            }
        },
        pipeline.stage(BUILD, StageStrategy.SKIP_STAGE) {
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assemble")
        },
        pipeline.stage(UNIT_TEST, StageStrategy.SKIP_STAGE) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/")
        },
        pipeline.stage(INSTRUMENTATION_TEST, StageStrategy.SKIP_STAGE) {
            AndroidPipelineHelper.instrumentationTestStageBodyAndroid(
                    script,
                    new AvdConfig(),
                    "debug",
                    getTestInstrumentationRunnerName,
                    new AndroidTestConfig(
                            "assembleAndroidTest",
                            "build/outputs/androidTest-results/instrumental",
                            "build/reports/androidTests/instrumental",
                            true,
                            0
                    )
            )
        },
        pipeline.stage(STATIC_CODE_ANALYSIS, StageStrategy.SKIP_STAGE) {
            AndroidPipelineHelper.staticCodeAnalysisStageBody(script)
        },
        pipeline.stage(DEPLOY_MODULES) {
            withArtifactoryCredentials(script) {
                AndroidUtil.withGradleBuildCacheCredentials(script) {
                    script.sh "./gradlew clean uploadArchives -PonlyUnstable=true -PdeployOnlyIfNotExist=true"
                    if (useBintrayDeploy) {
                        script.sh "./gradlew distributeArtifactsToBintray -PonlyUnstable=true -PdeployOnlyIfNotExist=true"
                    }
                }
            }
        },
        pipeline.stage(DEPLOY_GLOBAL_VERSION_PLUGIN, StageStrategy.SKIP_STAGE) {
            withArtifactoryCredentials(script) {
                script.sh "./gradlew generateDataForPlugin"
                script.sh "./gradlew :android-standard-version-plugin:uploadArchives"
            }
        },
        pipeline.stage(VERSION_PUSH, StageStrategy.SKIP_STAGE) {
            if (!skipIncrementVersion) {
                RepositoryUtil.setDefaultJenkinsGitUser(script)
                String globalConfigurationJsonStr = script.readFile(projectConfigurationFile)
                def globalConfiguration = new JsonSlurperClassic().parseText(globalConfigurationJsonStr)

                script.sh "git commit -a -m \"Increase project-snapshot version counter to " +
                        "$globalConfiguration.project_snapshot_version $RepositoryUtil.SKIP_CI_LABEL1 $RepositoryUtil.VERSION_LABEL1\""
                RepositoryUtil.push(script, pipeline.repoUrl, pipeline.repoCredentialsId)
            } else {
                script.echo "skip version push stage"
            }
        }
]


pipeline.finalizeBody = {
    def jenkinsLink = CommonUtil.getBuildUrlSlackLink(script)
    def message
    def success = Result.SUCCESS == pipeline.jobResult
    def checkoutAborted = pipeline.getStage(CHECKOUT).result == Result.ABORTED
    if (!success && !checkoutAborted) {
        def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
        message = "Deploy из ветки '${branchName}' не выполнен из-за этапов: ${unsuccessReasons}. ${jenkinsLink}"
    } else {
        message = "Deploy из ветки '${branchName}' успешно выполнен. ${jenkinsLink}"
    }
    //JarvisUtil.sendMessageToGroup(script, message, pipeline.repoUrl, "bitbucket", pipeline.jobResult)
}

pipeline.run()

// ============================================= ↓↓↓ JOB PROPERTIES CONFIGURATION ↓↓↓  ==========================================


static List<Object> initProperties(ScmPipeline ctx) {
    def script = ctx.script
    return [
            initBuildDiscarder(script),
            initParameters(script),
            initTriggers(script)
    ]
}

def static initBuildDiscarder(script) {
    return script.buildDiscarder(
            script.logRotator(
                    artifactDaysToKeepStr: '3',
                    artifactNumToKeepStr: '10',
                    daysToKeepStr: '60',
                    numToKeepStr: '200')
    )
}

def static initParameters(script) {
    return script.parameters([
            script.string(
                    name: "branchName_0",
                    description: 'Ветка с исходным кодом'
            ),
            script.booleanParam(
                    defaultValue: false,
                    name: "useBintrayDeploy",
                    description: 'Будет ли выполнен деплой на bintray помимо обычного деплоя на artifactory'
            ),
            script.booleanParam(
                    defaultValue: false,
                    name: "skipIncrementVersion",
                    description: 'Деплой артефактов без инкремента версии'
            )
    ])
}

def static initTriggers(script) {
    return script.pipelineTriggers([
            script.GenericTrigger(
                    genericVariables: [
                            [
                                    key  : "branchName",
                                    value: '$.push.changes[?(@.new.type == "branch")].new.name'
                            ]
                    ],
                    printContributedVariables: true,
                    printPostContent: true,
                    causeString: 'Triggered by Bitbucket',
                    regexpFilterExpression: '^(origin\\/)?project-snapshot\\/(.*)$',
                    regexpFilterText: '$branchName_0'
            ),
            script.pollSCM('')
    ])
}

// ============================================= ↑↑↑  END JOB PROPERTIES CONFIGURATION ↑↑↑  ==========================================

// ============ Utils =================
def static withArtifactoryCredentials(script, body) {
    script.withCredentials([
            script.usernamePassword(
                    credentialsId: "Artifactory_Deploy_Credentials",
                    usernameVariable: 'surf_maven_username',
                    passwordVariable: 'surf_maven_password')
    ]) {
        body()
    }
}