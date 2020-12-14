@Library('surf-lib@version-3.0.0-SNAPSHOT')
//  https://gitlab.com/surfstudio/infrastructure/tools/jenkins-pipeline-lib

import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.pipeline.pr.PrPipeline
import ru.surfstudio.ci.stage.SimpleStage
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.utils.android.config.AndroidTestConfig
import ru.surfstudio.ci.utils.android.config.AvdConfig

import static ru.surfstudio.ci.CommonUtil.extractValueFromEnvOrParamsAndRun

//Pipeline for check prs

// Stage names
def PRE_MERGE = 'PreMerge'
def CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT = 'Check Configuration Is Not Project Snapshot'
def CHECK_STABLE_MODULES_IN_ARTIFACTORY = 'Check Stable Modules In Artifactory'
def CHECK_STABLE_MODULES_NOT_CHANGED = 'Check Stable Modules Not Changed'
def CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE = 'Check Unstable Modules Do Not Became Stable'
def CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE = 'Check Modules In Dependency Tree Of Stable Module Also Stable'
def CHECK_RELEASE_NOTES_VALID = 'Check Release Notes Valid'
def CHECK_RELEASE_NOTES_CHANGED = 'Check Release Notes Changed'
def CHECKS_RESULT = 'All Checks Result'

def RELEASE_NOTES_DIFF = 'Release notes diff'

def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'

def BUILD_TEMPLATE = 'Template Build '
def INSTRUMENTATION_TEST_TEMPLATE = 'Template Instrumentation Test'

// git variables
def sourceBranch = ""
def destinationBranch = ""
def authorUsername = ""
def targetBranchChanged = false
def lastDestinationBranchCommitHash = ""

//parameters
final String SOURCE_BRANCH_PARAMETER = 'sourceBranch'
final String DESTINATION_BRANCH_PARAMETER = 'destinationBranch'
final String AUTHOR_USERNAME_PARAMETER = 'authorUsername'
final String TARGET_BRANCH_CHANGED_PARAMETER = 'targetBranchChanged'

// Other config
final String TEMP_FOLDER_NAME = "temp"
def stagesForProjectMode = [
        PRE_MERGE,
        RELEASE_NOTES_DIFF,
        BUILD,
        UNIT_TEST
]
def stagesForReleaseMode = [
        PRE_MERGE,
        CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT,
        CHECK_STABLE_MODULES_IN_ARTIFACTORY,
        CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE,
        CHECK_RELEASE_NOTES_VALID,
        CHECK_RELEASE_NOTES_CHANGED,
        CHECKS_RESULT,
        BUILD,
        UNIT_TEST,
        INSTRUMENTATION_TEST,
        STATIC_CODE_ANALYSIS,
        BUILD_TEMPLATE,
        INSTRUMENTATION_TEST_TEMPLATE
]
def stagesForTargetBranchChangedMode = [
        PRE_MERGE
]

def getTestInstrumentationRunnerName = { script, prefix ->
    def defaultInstrumentationRunnerGradleTaskName = "printTestInstrumentationRunnerName"
    return script.sh(
            returnStdout: true,
            script: "./gradlew -q :$prefix:$defaultInstrumentationRunnerGradleTaskName"
    ).split("\n").last()
}

//init
def script = this
def pipeline = new EmptyScmPipeline(script)

pipeline.init()

//configuration
pipeline.node = "android"
pipeline.propertiesProvider = {
    [
            script.buildDiscarder(
                    script.logRotator(
                            artifactDaysToKeepStr: '3',
                            artifactNumToKeepStr: '10',
                            daysToKeepStr: '30',
                            numToKeepStr: '100')
            ),
            PrPipeline.parameters(script),
            PrPipeline.triggers(script, pipeline.repoUrl),
            script.gitLabConnection(pipeline.gitlabConnection)
    ]
}


pipeline.preExecuteStageBody = { stage ->
    if (stage.name != PRE_MERGE) RepositoryUtil.notifyGitlabAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    RepositoryUtil.notifyGitlabAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
}

pipeline.initializeBody = {
    CommonUtil.printInitialStageStrategies(pipeline)

    script.echo "artifactory user: ${script.env.surf_maven_username}"
    script.echo "bintray user: ${script.env.surf_bintray_username}"

    //если триггером был webhook параметры устанавливаются как env, если запустили вручную, то устанавливается как params
    extractValueFromEnvOrParamsAndRun(script, SOURCE_BRANCH_PARAMETER) {
        value -> sourceBranch = value
    }
    extractValueFromEnvOrParamsAndRun(script, DESTINATION_BRANCH_PARAMETER) {
        value -> destinationBranch = value
    }
    extractValueFromEnvOrParamsAndRun(script, AUTHOR_USERNAME_PARAMETER) {
        value -> authorUsername = value
    }
    extractValueFromEnvOrParamsAndRun(script, TARGET_BRANCH_CHANGED_PARAMETER) {
        value -> targetBranchChanged = Boolean.valueOf(value)
    }

    configureStageSkipping(
            script,
            pipeline,
            targetBranchChanged,
            stagesForTargetBranchChangedMode,
            "Build triggered by target branch changes, run only ${stagesForTargetBranchChangedMode} stages"
    )

    configureStageSkipping(
            script,
            pipeline,
            isSourceBranchRelease(sourceBranch),
            stagesForReleaseMode,
            "Build triggered by release source branch, run only ${stagesForReleaseMode} stages"
    )

    configureStageSkipping(
            script,
            pipeline,
            isDestinationBranchProjectSnapshot(destinationBranch),
            stagesForProjectMode,
            "Build triggered by project destination branch, run only ${stagesForProjectMode} stages"
    )

    def buildDescription = targetBranchChanged ?
            "$sourceBranch to $destinationBranch: target branch changed" :
            "$sourceBranch to $destinationBranch"

    CommonUtil.setBuildDescription(script, buildDescription)
    CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)
}

pipeline.stages = [
        pipeline.stage(PRE_MERGE) {
            CommonUtil.safe(script) {
                script.sh "git reset --merge" //revert previous failed merge
                script.sh "git clean -fd"
            }
            script.git(
                    url: pipeline.repoUrl,
                    credentialsId: pipeline.repoCredentialsId,
                    branch: destinationBranch
            )

            lastDestinationBranchCommitHash = RepositoryUtil.getCurrentCommitHash(script)

            script.sh "git checkout origin/$sourceBranch"

            RepositoryUtil.saveCurrentGitCommitHash(script)

            //local merge with destination
            script.sh "git merge origin/$destinationBranch --no-ff"
        },

        pipeline.stage(RELEASE_NOTES_DIFF, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew generateReleaseNotesDiff -PrevisionToCompare=${lastDestinationBranchCommitHash}")
        },

        pipeline.stage(CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT) {
            script.sh "./gradlew checkConfigurationIsNotProjectSnapshotTask"
        },
        pipeline.stage(CHECK_STABLE_MODULES_IN_ARTIFACTORY, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            withArtifactoryCredentials(script) {
                script.echo "artifactory user: ${script.env.surf_maven_username}"
                script.sh("./gradlew checkStableArtifactsExistInArtifactoryTask")
            }

            withBintrayCredentials(script) {
                script.echo "bintray user: ${script.env.surf_bintray_username}"
                script.sh("./gradlew checkStableArtifactsExistInBintrayTask")
            }

        },
        pipeline.stage(CHECK_STABLE_MODULES_NOT_CHANGED, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkStableComponentsChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}")
        },
        pipeline.stage(CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkUnstableToStableChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}")
        },
        pipeline.stage(CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkStableComponentStandardDependenciesStableTask")
        },
        pipeline.stage(CHECK_RELEASE_NOTES_VALID, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkReleaseNotesContainCurrentVersion")
            script.sh("./gradlew checkReleaseNotesNotContainCyrillic")
        },
        pipeline.stage(CHECK_RELEASE_NOTES_CHANGED, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkReleaseNotesChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}")
        },
        pipeline.stage(CHECKS_RESULT) {
            script.sh "rm -rf $TEMP_FOLDER_NAME"
            def checksPassed = true
            [
                    CHECK_STABLE_MODULES_IN_ARTIFACTORY,
                    CHECK_STABLE_MODULES_NOT_CHANGED,
                    CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE,
                    CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE,
                    CHECK_RELEASE_NOTES_VALID,
                    CHECK_RELEASE_NOTES_CHANGED
            ].each { stageName ->
                def stageResult = pipeline.getStage(stageName).result
                checksPassed = checksPassed && (stageResult == Result.SUCCESS || stageResult == Result.NOT_BUILT)

                if (!checksPassed) {
                    script.echo "Stage '${stageName}' ${stageResult}"
                }
            }

            if (!checksPassed) {
                throw script.error("Checks Failed, see reason above ^^^")
            }
        },

        pipeline.stage(BUILD) {
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assemble")
        },
        pipeline.stage(BUILD_TEMPLATE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("echo \"androidStandardDebugDir=$workspace\n" +
                    "androidStandardDebugMode=true\n" +
                    "skipSamplesBuild=true\" > template/android-standard/androidStandard.properties")
            /**
             * assembleDebug is used for assembleAndroidTest with testBuildType=debug for Template.
             * Running assembleAndroidTest with testBuildType=qa could cause some problems with proguard settings
             */
            script.sh("./gradlew -p template clean build assembleQa assembleDebug --stacktrace")
        },
        pipeline.stage(UNIT_TEST) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/")
        },
        //TODO не работает после переезда на MVI
        pipeline.stage(INSTRUMENTATION_TEST_TEMPLATE, StageStrategy.SKIP_STAGE) {
            script.dir("template") {
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
                        ),
                        "Template Instrumentation Test"
                )
            }
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
        }
]
pipeline.finalizeBody = {
    if (pipeline.jobResult != Result.SUCCESS && pipeline.jobResult != Result.ABORTED) {
        def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
        def message = "Ветка ${sourceBranch} в состоянии ${pipeline.jobResult} из-за этапов: ${unsuccessReasons}; ${CommonUtil.getBuildUrlSlackLink(script)}"
        JarvisUtil.sendMessageToUser(script, message, authorUsername, "gitlab")
    }
}

pipeline.run()


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

def static withBintrayCredentials(script, body) {
    script.withCredentials([
            script.usernamePassword(
                    credentialsId: "Bintray_Deploy_Credentials",
                    usernameVariable: 'surf_bintray_username',
                    passwordVariable: 'surf_bintray_api_key')
    ]) {
        body()
    }
}

def configureStageSkipping(script, pipeline, isSkip, stageNames, message) {
    if (isSkip) {
        script.echo message
        pipeline.stages.each { stage ->
            if (!(stage instanceof SimpleStage)) {
                return
            }
            def executeStage = false
            stageNames.each { stageName ->
                executeStage = executeStage || (stageName == stage.getName())
            }
            if (!executeStage) {
                stage.strategy = StageStrategy.SKIP_STAGE
            }
        }
    }
}

def static isSourceBranchRelease(String sourceBranch) {
    return sourceBranch.startsWith("release/")
}

def static isDestinationBranchProjectSnapshot(String destinationBranch) {
    return destinationBranch.startsWith("project-snapshot")
}