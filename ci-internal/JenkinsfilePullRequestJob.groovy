@Library('surf-lib@version-2.0.0-SNAPSHOT') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/

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

def BUILD_STAGES = 'Build Stages'
def PRE_MERGE = 'PreMerge'
def CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT = 'Check Configuration Is Not Project Snapshot'
def CHECK_STABLE_MODULES_IN_ARTIFACTORY = 'Check Stable Modules In Artifactory'
def CHECK_STABLE_MODULES_NOT_CHANGED = 'Check Stable Modules Not Changed'
def CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE = 'Check Unstable Modules Do Not Bacame Stable'
def CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE = 'Check Modules In Dependency Tree Of Stable Module Also Stable'
def CHECK_RELEASE_NOTES_VALID = 'Check Release Notes Valid'
def CHECK_RELEASE_NOTES_CHANGED = 'Check Release Notes Changed'
def CHECKS_RESULT = 'Checks Result'
def CHECKS_BUILD_TEMPLATE = 'Checks Build Template'

def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'

// git variables
def sourceBranch = ""
def destinationBranch = ""
def authorUsername = ""
def targetBranchChanged = false
def lastDestinationBranchCommitHash = ""

//parameters
def final String SOURCE_BRANCH_PARAMETER = 'sourceBranch'
def final String DESTINATION_BRANCH_PARAMETER = 'destinationBranch'
def final String AUTHOR_USERNAME_PARAMETER = 'authorUsername'
def final String TARGET_BRANCH_CHANGED_PARAMETER = 'targetBranchChanged'

// Other config
def stagesForTargetBranchChangedMode = [PRE_MERGE, BUILD, UNIT_TEST, INSTRUMENTATION_TEST]

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
pipeline.propertiesProvider = { PrPipeline.properties(pipeline) }

pipeline.preExecuteStageBody = { stage ->
    if (stage.name != PRE_MERGE) RepositoryUtil.notifyBitbucketAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    if (stage.name != PRE_MERGE) RepositoryUtil.notifyBitbucketAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
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

    if (targetBranchChanged) {
        script.echo "Build triggered by target branch changes, run only ${stagesForTargetBranchChangedMode} stages"
        pipeline.forStages { stage ->
            if (!stage instanceof SimpleStage) {
                return
            }
            def executeStage = false
            for (stageNameForTargetBranchChangedMode in stagesForTargetBranchChangedMode) {
                executeStage = executeStage || (stageNameForTargetBranchChangedMode == stage.getName())
            }
            if (!executeStage) {
                stage.strategy = StageStrategy.SKIP_STAGE
            }
        }
    }

    def buildDescription = targetBranchChanged ?
            "$sourceBranch to $destinationBranch: target branch changed" :
            "$sourceBranch to $destinationBranch"

    CommonUtil.setBuildDescription(script, buildDescription)
    CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)
}

//region Stages
def preMergeStage = pipeline.stage(PRE_MERGE) {
    CommonUtil.safe(script) {
        script.sh "git reset --merge" //revert previous failed merge
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
}

def checkConfigurationIsNotProjectSnapshotStage = pipeline.stage(
        CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT
) {
    script.sh "./gradlew checkConfigurationIsNotProjectSnapshotTask"
}

def checkStableModulesInArtifactoryStage = pipeline.stage(
        CHECK_STABLE_MODULES_IN_ARTIFACTORY,
        StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
) {
    withArtifactoryCredentials(script) {
        script.echo "artifactory user: ${script.env.surf_maven_username}"
        script.sh("./gradlew checkStableArtifactsExistInArtifactoryTask")
    }

    withBintrayCredentials(script) {
        script.echo "bintray user: ${script.env.surf_bintray_username}"
        script.sh("./gradlew checkStableArtifactsExistInBintrayTask")
    }
}

def checkStableModulesNotChangedStage = pipeline.stage(
        CHECK_STABLE_MODULES_NOT_CHANGED,
        StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
) {
    script.sh("./gradlew checkStableComponentsChanged -PrevisionToCompare=" +
            "${lastDestinationBranchCommitHash}")
}

def checkModulesInDependencyTreeOfStableModuleAlsoStableStage = pipeline.stage(
        CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE,
        StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
) {
    script.sh("./gradlew checkStableComponentStandardDependenciesStableTask")
}

def checkUnstableModulesDoNotBecameStableStage =  pipeline.stage(
        CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE,
        StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
) {
    script.sh("./gradlew checkUnstableToStableChanged -PrevisionToCompare=" +
            "${lastDestinationBranchCommitHash}")
}

def checkReleaseNotChangedStage = pipeline.stage(
        CHECK_RELEASE_NOTES_VALID,
        StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
) {
    script.sh("./gradlew checkReleaseNotesContainCurrentVersion")
}

def checkReleaseNotesChangedStage = pipeline.stage(
        CHECK_RELEASE_NOTES_CHANGED,
        StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
) {
    script.sh("./gradlew checkReleaseNotesChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}")
}

def checkBuildTemplateStage = pipeline.stage(CHECKS_BUILD_TEMPLATE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
    script.sh("./gradlew generateModulesNamesFile")
    script.sh("echo \"androidStandardDebugDir=$workspace\n" +
            "androidStandardDebugMode=true\" > template/android-standard/androidStandard.properties")
    script.sh("./gradlew -p template clean build assembleQa --stacktrace")
}

def buildStage = pipeline.stage(BUILD) {
    AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assemble")
}

def unitTestStage = pipeline.stage(UNIT_TEST) {
    AndroidPipelineHelper.unitTestStageBodyAndroid(script,
            "testReleaseUnitTest",
            "**/test-results/testReleaseUnitTest/*.xml",
            "app/build/reports/tests/testReleaseUnitTest/")
}

def instrumentationTestStage = pipeline.stage(INSTRUMENTATION_TEST) {
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
}

def staticCodeAnalysisStage = pipeline.stage(STATIC_CODE_ANALYSIS, StageStrategy.SKIP_STAGE) {
    AndroidPipelineHelper.staticCodeAnalysisStageBody(script)
}

//Must be last in stages definition
def initStages = pipeline.stage(BUILD_STAGES) {
    if (isDestinationBranchProjectSnapshot(script, destinationBranch)) {
        script.echo "DEV_INFO 3"
        pipeline.stages.addAll(
                buildStage,
                unitTestStage
        )
    } else if (isSourceBranchRelease(sourceBranch)) {
        pipeline.stages.addAll(
                checkConfigurationIsNotProjectSnapshotStage,
                checkStableModulesInArtifactoryStage,
                checkModulesInDependencyTreeOfStableModuleAlsoStableStage,
                checkReleaseNotChangedStage,
                checkBuildTemplateStage,
                pipeline.stage(CHECKS_RESULT) {
                    def checksPassed = true
                    [
                            CHECK_STABLE_MODULES_IN_ARTIFACTORY,
                            CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE,
                            CHECK_RELEASE_NOTES_VALID,
                            CHECKS_BUILD_TEMPLATE
                    ].each { stageName ->
                        def stageResult = pipeline.getStage(stageName).result
                        checksPassed = checksPassed && (stageResult == Result.SUCCESS || stageResult == Result.NOT_BUILT)
                        if (!checksPassed) {
                            script.echo "stageName = ${stageName}, checksPassed = ${checksPassed}, stageResult = ${stageResult}"
                        }
                    }

                    if (!checksPassed) {
                        script.error("Checks Failed")
                    }
                },
                buildStage,
                unitTestStage,
                instrumentationTestStage,
                staticCodeAnalysisStage
        )
    } else {
        pipeline.stages.addAll(
                checkConfigurationIsNotProjectSnapshotStage,
                checkStableModulesInArtifactoryStage,
                checkStableModulesNotChangedStage,
                checkUnstableModulesDoNotBecameStableStage,
                checkModulesInDependencyTreeOfStableModuleAlsoStableStage,
                checkReleaseNotChangedStage,
                checkReleaseNotesChangedStage,
                checkBuildTemplateStage,
                pipeline.stage(CHECKS_RESULT) {
                    def checksPassed = true
                    [
                            CHECK_STABLE_MODULES_IN_ARTIFACTORY,
                            CHECK_STABLE_MODULES_NOT_CHANGED,
                            CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE,
                            CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE,
                            CHECK_RELEASE_NOTES_VALID,
                            CHECK_RELEASE_NOTES_CHANGED,
                            CHECKS_BUILD_TEMPLATE
                    ].each { stageName ->
                        def stageResult = pipeline.getStage(stageName).result
                        checksPassed = checksPassed && (stageResult == Result.SUCCESS || stageResult == Result.NOT_BUILT)
                        if (!checksPassed) {
                            script.echo "stageName = ${stageName}, checksPassed = ${checksPassed}, stageResult = ${stageResult}"
                        }
                    }

                    if (!checksPassed) {
                        script.error("Checks Failed")
                    }
                },
                buildStage,
                unitTestStage,
                instrumentationTestStage,
                staticCodeAnalysisStage
        )
    }
}
//endregion

pipeline.stages = [
        preMergeStage,
        initStages
]

pipeline.finalizeBody = {
    if (pipeline.jobResult != Result.SUCCESS && pipeline.jobResult != Result.ABORTED) {
        def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
        def message = "Ветка ${sourceBranch} в состоянии ${pipeline.jobResult} из-за этапов: ${unsuccessReasons}; ${CommonUtil.getBuildUrlMarkdownLink(script)}"
        JarvisUtil.sendMessageToUser(script, message, authorUsername, "bitbucket")
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

def static isSourceBranchRelease(String sourceBranch) {
    return sourceBranch.startsWith("release/")
}

def static isDestinationBranchProjectSnapshot(script, String destinationBranch) {
    script.echo "DEV_INFO 1 sourceBranch = $destinationBranch"
    script.echo "DEV_INFO 2 ${destinationBranch.startsWith("project-snapshot/")}"
    return destinationBranch.startsWith("project-snapshot/")
}