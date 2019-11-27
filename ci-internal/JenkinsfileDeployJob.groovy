@Library('surf-lib@version-1.0.0-SNAPSHOT') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
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
def CHECKOUT = 'Checkout'
def CHECK_BRANCH_AND_VERSION = 'Check Branch & Version'
def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'
def VERSION_INCREMENT = 'Version Increment'
def DEPLOY = 'Deploy'
def VERSION_PUSH = 'Version Push'

//constants
def gradleConfigFile = "config.gradle"
def androidStandardVersionVarName = "moduleVersionName"

//vars
def branchName = ""
def actualAndroidStandardVersion = "<unknown>"
//def removeSkippedBuilds = true - not work properly

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
pipeline.node = "android-2"

pipeline.preExecuteStageBody = { stage ->
    if(stage.name != CHECKOUT) RepositoryUtil.notifyBitbucketAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    if(stage.name != CHECKOUT) RepositoryUtil.notifyBitbucketAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
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

    if(branchName.contains("origin/")){
        branchName = branchName.replace("origin/", "")
    }

    if(isProjectSnapshotBranch(branchName)){
        script.echo "Apply lightweight strategies and automatic version increment for project-snapshot branch"
        pipeline.getStage(BUILD).strategy = StageStrategy.SKIP_STAGE
        pipeline.getStage(UNIT_TEST).strategy = StageStrategy.SKIP_STAGE
        pipeline.getStage(INSTRUMENTATION_TEST).strategy = StageStrategy.SKIP_STAGE
        pipeline.getStage(STATIC_CODE_ANALYSIS).strategy = StageStrategy.SKIP_STAGE
        pipeline.getStage(VERSION_INCREMENT).strategy = StageStrategy.FAIL_WHEN_STAGE_ERROR
        pipeline.getStage(VERSION_PUSH).strategy = StageStrategy.FAIL_WHEN_STAGE_ERROR
    }

    CommonUtil.safe(script){
        def buildLink = CommonUtil.getBuildUrlSlackLink(script)
        JarvisUtil.sendMessageToGroup(script, "Инициирован Deploy ветки ${branchName}. $buildLink", pipeline.repoUrl, "bitbucket", true)
    }

    def buildDescription = branchName
    CommonUtil.setBuildDescription(script, buildDescription)
    CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)
}

pipeline.stages = [
        pipeline.createStage(CHECKOUT, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            script.git(
                    url: pipeline.repoUrl,
                    credentialsId: pipeline.repoCredentialsId
            )
            script.sh "git checkout -B $branchName origin/$branchName"

            script.echo "Checking $RepositoryUtil.SKIP_CI_LABEL1 label in last commit message for automatic builds"
            if (RepositoryUtil.isCurrentCommitMessageContainsSkipCiLabel(script) && !CommonUtil.isJobStartedByUser(script)) {
                /*if(removeSkippedBuilds) {
                    script.currentBuild.rawBuild.delete() - not work properly, bug in blue ocean: job activity page 404
                }*/
                throw new InterruptedException("Job aborted, because it triggered automatically and last commit message contains $RepositoryUtil.SKIP_CI_LABEL1 label")
            }

            RepositoryUtil.saveCurrentGitCommitHash(script)
        },
        pipeline.createStage(CHECK_BRANCH_AND_VERSION, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            if (RepositoryUtil.isCurrentCommitMessageContainsVersionLabel(script)){
                script.echo "Disable automatic version increment because commit message contains $RepositoryUtil.VERSION_LABEL1"
                pipeline.getStage(VERSION_INCREMENT).strategy = StageStrategy.SKIP_STAGE
                pipeline.getStage(VERSION_PUSH).strategy = StageStrategy.SKIP_STAGE
            }
            def rawVersion = AndroidUtil.getGradleVariable(script, gradleConfigFile, androidStandardVersionVarName)
            actualAndroidStandardVersion = rawVersion
            def version = CommonUtil.removeQuotesFromTheEnds(rawVersion) //remove quotes
            def branch = branchName
            def originPrefix = "origin/"
            if(branch.contains(originPrefix)){
                branch = branch.substring(originPrefix.length())
            }
            /*
            script.echo "Checking branch: '$branch' and version: '$version'..."

            def masterChecked = checkVersionAndBranch(script,
                    branch, /^master$/,
                    version, /^\d{1,4}\.\d{1,4}\.\d{1,4}$/)

            def snapshotChecked = checkVersionAndBranch(script,
                    branch, /^snapshot-\d{1,4}\.\d{1,4}\.\d{1,4}$/,
                    version, /^\d{1,4}\.\d{1,4}\.\d{1,4}-SNAPSHOT$/)

            def projectSnapshotChecked = checkVersionAndBranchForProjectSnapshot(script, branch, version)

            if(!(masterChecked || snapshotChecked || projectSnapshotChecked)) {
                error("Deploy from branch: '$branch' forbidden")
            }
            */ //todo fix checks
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
    def jenkinsLink = CommonUtil.getBuildUrlSlackLink(script)
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