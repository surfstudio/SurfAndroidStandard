@Library('surf-lib@version-4.1.3-SNAPSHOT')
// https://github.com/surfstudio/jenkins-pipeline-lib

import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.pipeline.pr.PrPipeline
import ru.surfstudio.ci.stage.SimpleStage
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.utils.android.config.AndroidTestConfig
import ru.surfstudio.ci.utils.android.config.AvdConfig
import ru.surfstudio.ci.utils.buildsystems.GradleUtil

import static ru.surfstudio.ci.CommonUtil.extractValueFromEnvOrParamsAndRun

//Pipeline for check prs

// Stage names
def PRE_MERGE = 'PreMerge'
def CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT = 'Check Configuration Is Not Project Snapshot'
def CHECK_STABLE_MODULES_IN_ARTIFACTORY = 'Check Stable Modules In Artifactory'
def CHECK_STABLE_MODULES_NOT_CHANGED = 'Check Stable Modules Not Changed'
def CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE = 'Check Unstable Modules Do Not Became Stable'
def CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE = 'Check Modules In Dependency Tree Of Stable Module Also Stable'
def CHECK_RELEASE_NOTES_VALID = 'Check Release Notes Valid'
def CHECK_RELEASE_NOTES_CHANGED = 'Check Release Notes Changed'
def ADD_LICENSE = 'Add License'
def CHECKS_RESULT = 'All Checks Result'

def RELEASE_NOTES_DIFF = 'Release notes diff'

def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'

def BUILD_TEMPLATE = 'Template Build'
def INSTRUMENTATION_TEST_TEMPLATE = 'Template Instrumentation Test'

// variables
def sourceBranch = ""
def destinationBranch = ""
def authorUsername = ""
def targetBranchChanged = false
def lastDestinationBranchCommitHash = ""
def useJava11 = false // default value
def java11Branches = [
        "dev/G-0.5.0",
        "project-snapshot/BET",
        "project-snapshot/BZN"
]

//parameters
final String SOURCE_BRANCH_PARAMETER = 'sourceBranch'
final String DESTINATION_BRANCH_PARAMETER = 'destinationBranch'
final String AUTHOR_USERNAME_PARAMETER = 'authorUsername'
final String TARGET_BRANCH_CHANGED_PARAMETER = 'targetBranchChanged'

// Other config
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
        ADD_LICENSE,
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
    ]
}


pipeline.preExecuteStageBody = { stage ->
    if (stage.name != PRE_MERGE) RepositoryUtil.notifyGithubAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    RepositoryUtil.notifyGithubAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
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

    if (java11Branches.contains(destinationBranch)) {
        useJava11 = true
    }
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
            GradleUtil.gradlew(script, "generateReleaseNotesDiff -PrevisionToCompare=${lastDestinationBranchCommitHash}", useJava11)
        },

        pipeline.stage(CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT) {
            GradleUtil.gradlew(script, "checkConfigurationIsNotProjectSnapshotTask", useJava11)
        },
        pipeline.stage(CHECK_STABLE_MODULES_IN_ARTIFACTORY, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            withArtifactoryCredentials(script) {
                script.echo "artifactory user: ${script.env.surf_maven_username}"
                GradleUtil.gradlew(script, "checkStableArtifactsExistInArtifactoryTask", useJava11)
            }
        },
        //todo ANDDEP-1259
        pipeline.stage(CHECK_STABLE_MODULES_NOT_CHANGED, StageStrategy.SKIP_STAGE) {
            GradleUtil.gradlew(script, "checkStableComponentsChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}", useJava11)
        },
        pipeline.stage(CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE, StageStrategy.SKIP_STAGE) {
            GradleUtil.gradlew(script, "checkUnstableToStableChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}", useJava11)
        },
        pipeline.stage(CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            GradleUtil.gradlew(script, "checkStableComponentStandardDependenciesStableTask", useJava11)
        },
        pipeline.stage(CHECK_RELEASE_NOTES_VALID, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            GradleUtil.gradlew(script, "checkReleaseNotesContainCurrentVersion", useJava11)
            GradleUtil.gradlew(script, "checkReleaseNotesNotContainCyrillic", useJava11)
        },
        //todo ANDDEP-1259
        pipeline.stage(CHECK_RELEASE_NOTES_CHANGED, StageStrategy.SKIP_STAGE) {
            GradleUtil.gradlew(script, "checkReleaseNotesChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}", useJava11)
        },
        pipeline.stage(ADD_LICENSE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh "chmod 755 ci-internal/auto-add-license/add_license.sh"
            script.sh "./ci-internal/auto-add-license/add_license.sh"
        },
        pipeline.stage(CHECKS_RESULT) {
            def checksPassed = true
            [
                    CHECK_STABLE_MODULES_IN_ARTIFACTORY,
                    CHECK_STABLE_MODULES_NOT_CHANGED,
                    CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE,
                    CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE,
                    CHECK_RELEASE_NOTES_VALID,
                    CHECK_RELEASE_NOTES_CHANGED,
                    ADD_LICENSE
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
            script.sh("rm -rf temp template/**/build")
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assembleQa", useJava11)
        },
        pipeline.stage(BUILD_TEMPLATE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("echo \"androidStandardDebugDir=$workspace\n" +
                    "androidStandardDebugMode=true\n" +
                    "skipSamplesBuild=true\" > template/android-standard/androidStandard.properties")
            /**
             * assembleDebug is used for assembleAndroidTest with testBuildType=debug for Template.
             * Running assembleAndroidTest with testBuildType=qa could cause some problems with proguard settings
             */
            AndroidPipelineHelper.buildStageBodyAndroid(script, "-p template clean assembleQa --stacktrace", useJava11)
        },
        pipeline.stage(UNIT_TEST) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(
                    script,
                    "testQaUnitTest",
                    "**/test-results/testQaUnitTest/*.xml",
                    "app/build/reports/tests/testQaUnitTest/",
                    useJava11
            )
        },
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
        JarvisUtil.sendMessageToUser(script, message, authorUsername, "github")
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