@Library('surf-lib@version-4.1.3-SNAPSHOT')
// https://github.com/surfstudio/jenkins-pipeline-lib

import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.pipeline.pr.PrPipeline
import ru.surfstudio.ci.stage.SimpleStage
import ru.surfstudio.ci.stage.StageStrategy

import static ru.surfstudio.ci.CommonUtil.extractValueFromEnvOrParamsAndRun
//Pipeline for check prs

// !!! Job оставлен для обратной совместимости.
// Актуальная версия переписана на Github Actions, см .github/workflows/pull_request.yml
// При необходимости можно включить job Standard_Android_PR в jenkins

// Stage names
def PRE_MERGE = 'PreMerge'

def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'

def BUILD_TEMPLATE = 'Template Build'

// variables
def sourceBranch = ""
def destinationBranch = ""
def authorUsername = ""
def targetBranchChanged = false
def lastDestinationBranchCommitHash = ""
def useJava11 = true // default value

//parameters
final String SOURCE_BRANCH_PARAMETER = 'sourceBranch'
final String DESTINATION_BRANCH_PARAMETER = 'destinationBranch'
final String AUTHOR_USERNAME_PARAMETER = 'authorUsername'
final String TARGET_BRANCH_CHANGED_PARAMETER = 'targetBranchChanged'

// Other config
def stagesForProjectMode = [
        PRE_MERGE,
        BUILD,
        UNIT_TEST
]
def stagesForReleaseMode = [
        PRE_MERGE,
        BUILD,
        UNIT_TEST,
        BUILD_TEMPLATE,
]
def stagesForTargetBranchChangedMode = [
        PRE_MERGE
]

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
        pipeline.stage(BUILD) {
            script.sh("rm -rf temp template/**/build")
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assembleQa", useJava11)
        },
        pipeline.stage(BUILD_TEMPLATE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("echo \"androidStandardDebugDir=$workspace\n" +
                    "androidStandardDebugMode=true\" > template/android-standard/androidStandard.properties")
            /**
             * assembleDebug is used for assembleAndroidTest with testBuildType=debug for Template.
             * Running assembleAndroidTest with testBuildType=qa could cause some problems with proguard settings
             */
            AndroidPipelineHelper.buildStageBodyAndroid(script, "-p template clean assembleQa assembleRelease --stacktrace", useJava11)
        },
        pipeline.stage(UNIT_TEST) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(
                    script,
                    "testQaUnitTest",
                    "**/test-results/testQaUnitTest/*.xml",
                    "app/build/reports/tests/testQaUnitTest/",
                    useJava11
            )
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