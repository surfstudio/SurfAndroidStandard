@Library('surf-lib@version-3.0.0-SNAPSHOT')
// https://gitlab.com/surfstudio/infrastructure/tools/jenkins-pipeline-lib
import groovy.json.JsonSlurperClassic
import ru.surfstudio.ci.*
import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.ScmPipeline
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.utils.android.AndroidUtil
import ru.surfstudio.ci.utils.android.config.AndroidTestConfig
import ru.surfstudio.ci.utils.android.config.AvdConfig

//Pipeline for deploy project snapshot artifacts

// Stage names

def CHECKOUT = 'Checkout'
def NOTIFY_ABOUT_NEW_RELEASE_NOTES = 'Notify About New Release Notes'
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
def releaseNotesChangesFileUrl = "buildSrc/build/tmp/releaseNotesChanges.txt"
def idChatAndroidStandardSlack = "CFS619TMH"// #android-standard

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
    if (stage.name != CHECKOUT) RepositoryUtil.notifyGitlabAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    if (stage.name != CHECKOUT) RepositoryUtil.notifyGitlabAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
}

pipeline.initializeBody = {
    CommonUtil.printInitialStageStrategies(pipeline)

    script.echo "artifactory user: ${script.env.surf_maven_username}"

    //Выбираем значения веток из параметров, Установка их в параметры происходит
    // если триггером был webhook или если стартанули Job вручную
    //Используется имя branchName_0 из за особенностей jsonPath в GenericWebhook plugin
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'branchName') {
        value -> branchName = value
    }
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'useBintrayDeploy') {
        value -> useBintrayDeploy = Boolean.valueOf(value)
    }
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'skipIncrementVersion') {
        value -> skipIncrementVersion = Boolean.valueOf(value)
    }

    branchName = branchName.replace("refs/heads/", "")
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
                scmSkip(deleteBuild: true, skipPattern: '.*\\[skip ci\\].*')
            }

            RepositoryUtil.saveCurrentGitCommitHash(script)
            RepositoryUtil.checkLastCommitMessageContainsSkipCiLabel(script)
        },
        pipeline.stage(NOTIFY_ABOUT_NEW_RELEASE_NOTES, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR, false) {
            def commitParents = script.sh(returnStdout: true, script: 'git log -1  --pretty=%P').split(' ')
            def prevCommitHash = commitParents[0]
            script.sh("./gradlew WriteToFileReleaseNotesDiffForSlack -PrevisionToCompare=${prevCommitHash}")
            String releaseNotesChanges = script.readFile(releaseNotesChangesFileUrl)

            if (releaseNotesChanges.trim() != "") {
                boolean isAddedNoBackwardCompatibility = releaseNotesChanges.split("\n")
                        .any {
                            it.contains(":heavy_plus_sign:") && it.contains("NO BACKWARD COMPATIBILITY")
                        }

                def messageTitle = "Snapshot branch _${branchName}_ has changed"
                if (isAddedNoBackwardCompatibility) {
                    messageTitle += " *without backward compatibility*:warning:"
                }

                releaseNotesChanges = "$messageTitle\n$releaseNotesChanges"
                JarvisUtil.sendMessageToGroup(script, releaseNotesChanges, idChatAndroidStandardSlack, "slack", true)
            }
        },
        pipeline.stage(CHECK_BRANCH_AND_VERSION) {
            String globalConfigurationJsonStr = script.readFile(projectConfigurationFile)
            def globalConfiguration = new JsonSlurperClassic().parseText(globalConfigurationJsonStr)
            project = globalConfiguration.project_snapshot_name

            if (("project-snapshot/" + project) != branchName) {
                script.error("Deploy AndroidStandard for project: $project from branch: '$branchName' forbidden")
            }
        },
        pipeline.stage(CHECK_CONFIGURATION_IS_PROJECT_SNAPHOT) {
            script.sh("./gradlew checkConfigurationIsProjectSnapshotTask")
        },
        pipeline.stage(INCREMENT_PROJECT_SNAPSHOT_VERSION) {
            if (!skipIncrementVersion) {
                script.sh("./gradlew incrementProjectSnapshotVersion")
            } else {
                script.echo "skip project snapshot version incrementation stage"
            }
        },
        pipeline.stage(BUILD) {
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assemble")
        },
        pipeline.stage(UNIT_TEST) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(
                    script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/"
            )
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
                    script.sh "./gradlew clean uploadArchives -PdeployOnlyIfNotExist=true"
                    if (useBintrayDeploy) {
                        /**
                         * We can not use parameter -PdeployOnlyIfNotExist
                         * for distributeArtifactsToBintray after uploadArchives,
                         * otherwise deploy to Bintray will never be executed
                         * after successful deploy to artifactory
                         */
                        script.sh "./gradlew distributeArtifactsToBintray"
                    }
                }
            }
        },
        pipeline.stage(DEPLOY_GLOBAL_VERSION_PLUGIN) {
            withArtifactoryCredentials(script) {
                script.sh "./gradlew generateDataForPlugin"
                script.sh "./gradlew :android-standard-version-plugin:uploadArchives"
            }
        },
        pipeline.stage(VERSION_PUSH, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
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
            if (useBintrayDeploy) {
                message += "\nБыл выполнен деплой артефактов в Bintray.\n" +
                        "Необходимо заменить последние версии артефактов в Bintray на стабильные"
            }
        }

        JarvisUtil.sendMessageToGroup(script, message, pipeline.repoUrl, "gitlab", pipeline.jobResult)
    }
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
                    numToKeepStr: '200'
            )
    )
}

def static initParameters(script) {
    return script.parameters([
            script.string(
                    name: "branchName",
                    description: 'Ветка с исходным кодом'
            ),
            script.booleanParam(
                    defaultValue: false,
                    name: "useBintrayDeploy",
                    description: 'Будет ли выполнен деплой на bintray помимо обычного деплоя на artifactory.\n' +
                            'Необходимо перед передачей проекта заказчику'
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
                                    value: '$.ref'
                            ]
                    ],
                    printContributedVariables: true,
                    printPostContent: true,
                    causeString: 'Triggered by Gitlab',
                    regexpFilterExpression: '^(origin\\/)?refs\\/heads\\/project-snapshot\\/(.*)$',
                    regexpFilterText: '$branchName'
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
                    passwordVariable: 'surf_maven_password'
            )
    ]) {
        body()
    }
}