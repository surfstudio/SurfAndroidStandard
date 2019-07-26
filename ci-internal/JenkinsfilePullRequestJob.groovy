
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
def CHECK_MODULES_DO_NOT_BECAME_STABLE = 'Check Modules Do Not Bacame Stable'
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
        pipeline.createStage(PRE_MERGE, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            CommonUtil.safe(script) {
                script.sh "git reset --merge" //revert previous failed merge
            }

            script.git(
                    url: pipeline.url,
                    credentialsId: pipeline.credentialsId,
                    branch: sourceBranch
            )

            RepositoryUtil.saveCurrentGitCommitHash(script)

            //local merge with destination
            script.sh "git merge origin/$destinationBranch --no-ff"
        },
        pipeline.createStage(CHECK_STABLE_MODULES_IN_ARTIFACTORY, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            script.sh("./gradlew ")
        },
        pipeline.createStage(BUILD, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assembleRelease assembleDebug")
        },
        pipeline.createStage(UNIT_TEST, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            AndroidPipelineHelper.unitTestStageBodyAndroid(script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/")
        },
        pipeline.createStage(INSTRUMENTATION_TEST, StageStrategy.FAIL_WHEN_STAGE_ERROR) {
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
        pipeline.createStage(STATIC_CODE_ANALYSIS, StageStrategy.SKIP_STAGE) {
            AndroidPipelineHelper.staticCodeAnalysisStageBody(script)
        },
        pipeline.createStage(VERSION_INCREMENT, StageStrategy.SKIP_STAGE) {
            def rawVersion = AndroidUtil.getGradleVariable(script, gradleConfigFile, androidStandardVersionVarName)
            String[] versionParts = rawVersion.split("-")
            String newSnapshotVersion = String.valueOf(Integer.parseInt(versionParts[2]) + 1)
            newRawVersion = versionParts[0] + "-" + versionParts[1] + "-" + newSnapshotVersion + "-" + versionParts[3]
            AndroidUtil.changeGradleVariable(script, gradleConfigFile, androidStandardVersionVarName, newRawVersion)
            actualAndroidStandardVersion = newRawVersion
        },
        pipeline.createStage(DEPLOY, StageStrategy.FAIL_WHEN_STAGE_ERROR) {
            AndroidUtil.withGradleBuildCacheCredentials(script) {
                script.sh "./gradlew clean uploadArchives"
            }
        },
        pipeline.createStage(VERSION_PUSH, StageStrategy.SKIP_STAGE) {
            def version = CommonUtil.removeQuotesFromTheEnds(
                    AndroidUtil.getGradleVariable(script, gradleConfigFile, androidStandardVersionVarName))
            RepositoryUtil.setDefaultJenkinsGitUser(script)
            script.sh "git commit -a -m \"Increase version to $version $RepositoryUtil.SKIP_CI_LABEL1 $RepositoryUtil.VERSION_LABEL1\""
            RepositoryUtil.push(script, pipeline.repoUrl, pipeline.repoCredentialsId)
        }
]

pipeline.finalizeBody = {
    def jenkinsLink = CommonUtil.getBuildUrlMarkdownLink(script)
    def message
    def success = Result.SUCCESS.equals(pipeline.jobResult)
    if (!success) {
        if (pipeline.getStage(CHECKOUT).result != Result.ABORTED) { //change notification for bilds, wich aborted via [skip ci] label
            def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
            message = "Deploy версии: $actualAndroidStandardVersion из ветки '${branchName}' не выполнен из-за этапов: ${unsuccessReasons}. ${jenkinsLink}"
        } else {
            message = "Deploy из ветки '${branchName}' остановлен из-за метки [skip ci] в последнем коммите"
        }
    } else {
        message = "Deploy версии: $actualAndroidStandardVersion из ветки '${branchName}' успешно выполнен. ${jenkinsLink}"
    }
    JarvisUtil.sendMessageToGroup(script, message, pipeline.repoUrl, "bitbucket", success)
}

pipeline.run()



// UTILS

def static isProjectSnapshotBranch(branchName) {
    branchName.contains("project-snapshot")
}

static boolean checkVersionAndBranch(Object script, String branch, String branchRegex, String version, String versionRegex) {
    Pattern branchPattern = Pattern.compile(branchRegex)
    Matcher branchMatcher =  branchPattern.matcher(branch)
    if(branchMatcher.matches()) {
        Pattern versionPattern = Pattern.compile(versionRegex)
        Matcher versionMatcher =  versionPattern.matcher(version)
        if (versionMatcher.matches()) {
            return true
        } else {
            script.error("Deploy version: '$version' from branch: '$branch' forbidden, it must corresponds to regexp: '$versionRegex'")
        }
    }
    return false
}

static boolean checkVersionAndBranchForProjectSnapshot(Object script, String branch, String version) {
    def branchRegex = /^project-snapshot-[A-Z]+/
    Pattern branchPattern = Pattern.compile(branchRegex)
    Matcher branchMatcher =  branchPattern.matcher(branch)
    if(branchMatcher.matches()) {
        def projectKey = branch.split('-')[2]
        def versionRegex = /^\d{1,4}\.\d{1,4}\.\d{1,4}-$projectKey-\d{1,4}-SNAPSHOT$/
        Pattern versionPattern = Pattern.compile(versionRegex)
        Matcher versionMatcher =  versionPattern.matcher(version)
        if (versionMatcher.matches()) {
            return true
        } else {
            script.error("Deploy version: '$version' from branch: '$branch' forbidden, it must corresponds to regexp: '$versionRegex'")
        }
    }
    return false
}


