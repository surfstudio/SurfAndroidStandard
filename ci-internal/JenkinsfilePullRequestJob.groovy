
@Library('surf-lib@version-2.0.0-SNAPSHOT') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
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
import java.util.regex.Pattern
import java.util.regex.Matcher

import static ru.surfstudio.ci.CommonUtil.applyParameterIfNotEmpty

//Кастомный пайплайн для деплоя артефактов

// Имена Шагов

def PRE_MERGE = 'PreMerge'
//def CHECK_BRANCH_AND_VERSION = 'Check Branch & Version' //todo ??? все пр одинаково запускать?
def CHECK_STABLE_MODULES_IN_ARTIFACTORY = 'Check Stable Modules In Artifactory'
def CHECK_STABLE_MODULES_NOT_CHANGED = 'Check Stable Modules Not Changed'
def CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE = 'Check Unstable Modules Do Not Bacame Stable'
def CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE = 'Check Modules In Dependency Tree Of Stable Module Also Stable'
def CHECK_RELEASE_NOTES_VALID = 'Check Relese Notes Valid'
def CHECK_RELEASE_NOTES_CHANGED = 'Check Relese Notes Changed'
def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'



//constants
/*def gradleConfigFile = "config.gradle"
def androidStandardVersionVarName = "moduleVersionName"

//vars
def branchName = ""
def actualAndroidStandardVersion = "<unknown>"
//def removeSkippedBuilds = true - not work properly*/

def sourceBranch = ""
def destinationBranch = ""
def authorUsername = ""
def targetBranchChanged = false
def lastDestinationBranchCommitHash = ""

//other config
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

pipeline.preExecuteStageBody = { stage ->
    if(stage.name != PRE_MERGE) RepositoryUtil.notifyBitbucketAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    if(stage.name != PRE_MERGE) RepositoryUtil.notifyBitbucketAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
}

pipeline.initializeBody = {
    CommonUtil.printInitialStageStrategies(pipeline)

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

    if(targetBranchChanged) {
        script.echo "Build triggered by target branch changes, run only ${stagesForTargetBranchChangedMode} stages"
        pipeline.forStages { stage ->
            if(!stage instanceof SimpleStage){
                return
            }
            def executeStage = false
            for(stageNameForTargetBranchChangedMode in stagesForTargetBranchChangedMode){
                executeStage = executeStage || (stageNameForTargetBranchChangedMode == stage.getName())
            }
            if(!executeStage) {
                stage.strategy = StageStrategy.SKIP_STAGE
            }
        }
    }

    def buildDescription = ctx.targetBranchChanged ?
            "$sourceBranch to $destinationBranch: target branch changed" :
            "$sourceBranch to $destinationBranch"

    CommonUtil.setBuildDescription(script, buildDescription)
    CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)
}

pipeline.stages = [
        pipeline.stage(PRE_MERGE){
            CommonUtil.safe(script) {
                script.sh "git reset --merge" //revert previous failed merge
            }

            script.git(
                    url: pipeline.url,
                    credentialsId: pipeline.credentialsId,
                    branch: destinationBranch
            )

            lastDestinationBranchCommitHash = RepositoryUtil.getCurrentCommitHash(script)

            script.sh "git checkout origin/$sourceBranch"

            RepositoryUtil.saveCurrentGitCommitHash(script)

            //local merge with destination
            script.sh "git merge origin/$destinationBranch --no-ff"
        },
        pipeline.stage(CHECK_STABLE_MODULES_IN_ARTIFACTORY){
            script.sh("./gradlew checkStableArtifactsExistInArtifactoryTask")
            script.sh("./gradlew checkStableArtifactsExistInBintrayTask")
        },
        pipeline.stage(CHECK_STABLE_MODULES_NOT_CHANGED){
            script.sh("./gradlew checkStableComponentsChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}")
        },
        pipeline.stage(CHECK_UNSTABLE_MODULES_DO_NOT_BECAME_STABLE){
            script.sh("./gradlew checkUnstableToStableChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}")
        },
        pipeline.stage(CHECK_MODULES_IN_DEPENDENCY_TREE_OF_STABLE_MODULE_ALSO_STABLE){
            script.sh("./gradlew checkStableComponentStandardDependenciesStableTask")
        },
        pipeline.stage(CHECK_RELEASE_NOTES_VALID){
            script.sh("./gradlew checkReleaseNotesContainCurrentVersion") //todo check for all components
        },

        pipeline.stage(CHECK_RELEASE_NOTES_CHANGED){
            script.sh("./gradlew checkReleaseNotesChanged -PrevisionToCompare=${lastDestinationBranchCommitHash}")
        },

        pipeline.stage(BUILD){
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assemble")
        },
        pipeline.stage(UNIT_TEST){
            AndroidPipelineHelper.unitTestStageBodyAndroid(script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/")
        },
        pipeline.stage(INSTRUMENTATION_TEST) {
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
        def message = "Ветка ${sourceBranch} в состоянии ${pipeline.jobResult} из-за этапов: ${unsuccessReasons}; ${CommonUtil.getBuildUrlMarkdownLink(script)}"
        JarvisUtil.sendMessageToUser(script, message, authorUsername, "bitbucket")
    }
}

pipeline.run()


