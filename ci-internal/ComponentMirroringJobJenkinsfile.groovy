@Library('surf-lib@version-2.0.0-SNAPSHOT')
import groovy.json.JsonSlurperClassic
import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.ScmPipeline
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline

// https://bitbucket.org/surfstudio/jenkins-pipeline-lib/

//Pipeline for deploy snapshot artifacts

// Stage names
def CHECKOUT = 'Checkout'
def MIRROR_COMPONENT = 'Mirror Component'

//constants
def componentsJsonFile = "buildSrc/components.json"
def MIRROR_FOLDER = ".mirror"
def DEPTH_LIMIT = 100
def SEARCH_LIMIT = 100

//vars
def branchName = ""
def lastCommit = ""
def components

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
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'branch') {
        value -> branchName = value
    }
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'lastCommit') {
        value -> lastCommit = value
    }

    if (branchName.contains("origin/")) {
        branchName = branchName.replace("origin/", "")
    }

    def buildDescription = branchName
    CommonUtil.setBuildDescription(script, buildDescription)
    CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)


//    String componentsJsonStr = script.readFile(componentsJsonFile)
//    components = new JsonSlurperClassic().parseText(componentsJsonStr)
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
        }
]

get_components(script).each { component ->
    pipeline.stages.add(
            pipeline.stage("$MIRROR_COMPONENT : ${component.id}", StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
                script.sh "git clone ${component.mirror_repo} $MIRROR_FOLDER"
//                script.sh "git deployToMirror -Pcomponent=${component.id} -Pcommit=$lastCommit " +
//                        "-PmirrorDir=$MIRROR_FOLDER -PdepthLimit=$DEPTH_LIMIT -PsearchLimit=$SEARCH_LIMIT"
                script.sh "rm -rf $MIRROR_FOLDER"
            }
    )
}

void get_components(script){
    def content = script.readFile("buildSrc/components.json")
    return new JsonSlurperClassic().parseText(content)
}

pipeline.finalizeBody = {
    def jenkinsLink = CommonUtil.getBuildUrlMarkdownLink(script)
    def message
    def success = Result.SUCCESS == pipeline.jobResult
    def checkoutAborted = pipeline.getStage(CHECKOUT).result == Result.ABORTED
    if (!success && !checkoutAborted) {
        def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
        message = "Deploy из ветки '${branchName}' не выполнен из-за этапов: ${unsuccessReasons}. ${jenkinsLink}"
    } else {
        message = "Deploy из ветки '${branchName}' успешно выполнен. ${jenkinsLink}"
    }
    JarvisUtil.sendMessageToGroup(script, message, pipeline.repoUrl, "bitbucket", success)

}

pipeline.run()

// ============================================= ↓↓↓ JOB PROPERTIES CONFIGURATION ↓↓↓  ==========================================


def static List<Object> initProperties(ScmPipeline ctx) {
    def script = ctx.script
    return [
            initDiscarder(script),
            initParameters(script)
    ]
}

def static initDiscarder(script) {
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
                    name: "branch",
                    description: 'Ветка с исходным кодом'
            ),
            script.string(
                    name: "lastCommit",
                    description: 'Коммит для зеркалирования'
            )
    ])
}

// ============================================= ↑↑↑  END JOB PROPERTIES CONFIGURATION ↑↑↑  ==========================================

// ============ Utils =================

def static getCommitHash(script, commit) {
    def parts = commit.split(" ")
    for (part in parts) {
        if (part.trim().matches("^[a-zA-Z0-9]+\$")) {
            return part.trim()
        }
    }
    script.error("Commit hash not found in commit str: $commit")
}

def static getPreviousRevisionWithVersionIncrement(script) {
    def commits = script.sh(
            returnStdout: true,
            script: "git  --no-pager log --pretty=oneline -500 --graph")
            .trim()
            .split("\n")

    def filteredCommits = []
    for (commit in commits) {
        if (commit.startsWith("*")) {
            //filter only commit in
            filteredCommits.add(commit)
        }
    }
    def revisionToCompare = null

    for (commit in filteredCommits) {
        if (commit.contains(RepositoryUtil.VERSION_LABEL1)) {
            script.echo("revision to compare: ${commit}")
            revisionToCompare = getCommitHash(script, commit)
            break
        }
    }
    if (revisionToCompare == null) {
        //gets previous commit
        def previousCommit
        if (commits[1] != "|\\  ") {
            previousCommit = commits[1]
        } else {
            previousCommit = commits[2]
        }
        script.echo("Not found revision with version label, so use previous revision to compare: ${previousCommit}")
        revisionToCompare = getCommitHash(script, previousCommit)
    }
    return revisionToCompare
}

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

def static withGradleBuildCacheCredentials(Object script, Closure body) {
    script.withCredentials([
            script.usernamePassword(
                    credentialsId: "gradle_build_cache",
                    usernameVariable: 'GRADLE_BUILD_CACHE_USER',
                    passwordVariable: 'GRADLE_BUILD_CACHE_PASS')
    ]) {
        body()
    }
}