@Library('surf-lib@version-2.0.0-SNAPSHOT')
// https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.ScmPipeline
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline

//Pipeline for deploy snapshot artifacts

// Stage names
def CHECKOUT = 'Checkout'
def CHECK_TAGS_FOR_RELEASE_ARTIFACTS = "Check Tags For Release Artifacts"
def CHECK_BINTRAY_STABLE_VERSIONS = 'Check Bintray Stable Versions'

//vars
def UNDEFINED_BRANCH = "<undefined>"
def branchName = UNDEFINED_BRANCH

//init
def script = this
def pipeline = new EmptyScmPipeline(script)

pipeline.init()

//configuration
pipeline.node = "android"
pipeline.propertiesProvider = { initProperties(pipeline, UNDEFINED_BRANCH) }

pipeline.preExecuteStageBody = { stage ->
    if (stage.name != CHECKOUT) RepositoryUtil.notifyBitbucketAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    if (stage.name != CHECKOUT) RepositoryUtil.notifyBitbucketAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
}

pipeline.initializeBody = {
    CommonUtil.printInitialStageStrategies(pipeline)

    CommonUtil.extractValueFromParamsAndRun(script, 'branchName_0') {
        value -> branchName = value
    }

    if (!branchName || branchName == UNDEFINED_BRANCH) {
        branchName = JarvisUtil.getMainBranch(script, pipeline.repoUrl)
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
            RepositoryUtil.saveCurrentGitCommitHash(script)
        },
        pipeline.stage(CHECK_TAGS_FOR_RELEASE_ARTIFACTS, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh "./gradlew checkTagsForReleaseArtifactsTask"
        },
        pipeline.stage(CHECK_BINTRAY_STABLE_VERSIONS) {
            script.sh "./gradlew checkBintrayStableVersionsTask"
        }
]

pipeline.finalizeBody = {
    def jenkinsLink = CommonUtil.getBuildUrlSlackLink(script)
    def message
    def success = Result.SUCCESS == pipeline.jobResult
    def unstable = Result.UNSTABLE == pipeline.jobResult
    def checkoutAborted = pipeline.getStage(CHECKOUT).result == Result.ABORTED
    if (!success && !checkoutAborted) {
        def errorReasons = "из ветки '${branchName}' ${unsuccessReasons} ${jenkinsLink}"
        if (unstable) {
            message = "Ошибка проверки наличия релизных тегов артефактов $errorReasons"
        } else {
            message = "Ошибка проверки стабильных версий артефактов на Bintray $errorReasons"
        }
        JarvisUtil.sendMessageToGroup(script, message, pipeline.repoUrl, "bitbucket", pipeline.jobResult)
    }
}

pipeline.run()

// ============================================= ↓↓↓ JOB PROPERTIES CONFIGURATION ↓↓↓  ==========================================

static List<Object> initProperties(ScmPipeline ctx, String defaultBranch) {
    def script = ctx.script
    return [
            initDiscarder(script),
            initParameters(script, defaultBranch)
    ]
}

def static initDiscarder(script) {
    return script.buildDiscarder(
            script.logRotator(
                    artifactDaysToKeepStr: '3',
                    artifactNumToKeepStr: '10',
                    daysToKeepStr: '30',
                    numToKeepStr: '20')
    )
}

def static initParameters(script, defaultBranch) {
    return script.parameters([
            script.string(
                    name: "branchName_0",
                    defaultValue: defaultBranch,
                    description: 'Ветка с исходным кодом'
            )
    ])
}

// ============================================= ↑↑↑  END JOB PROPERTIES CONFIGURATION ↑↑↑  ==========================================