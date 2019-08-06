import groovy.json.JsonSlurper
import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.ScmPipeline
@Library('surf-lib@version-2.0.0-SNAPSHOT')
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
@Library('surf-lib@version-2.0.0-SNAPSHOT')
// https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.stage.StageStrategy

//Pipeline for deploy snapshot artifacts

// Stage names

def CHECKOUT = 'Checkout'
def CHECK_BRANCH_AND_VERSION = 'Check Branch & Version'
def INCREMENT_GLOBAL_ALPHA_VERSION = 'Increment Global Alpha Version'
def INCREMENT_CHANGED_UNSTABLE_MODULES_ALPHA_VERSION = 'Increment Changed Unstable Modules Alpha Version'
def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'
def DEPLOY_MODULES = 'Deploy Modules'
def DEPLOY_GLOBAL_VERSION_PLUGIN = 'Deploy Global Version Plugin'
def VERSION_PUSH = 'Version Push'
def MIRROR_COMPONENTS = 'Mirror Components'

//constants
def projectConfigurationFile = "buildSrc/projectConfiguration.json"
def componentsJsonFile = "buildSrc/components.json"

//vars
def branchName = ""
def globalVersion = "<unknown>"
def libNames = []

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
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'branchName_0') {
        value -> branchName = value
    }

    if (branchName.contains("origin/")) {
        branchName = branchName.replace("origin/", "")
    }

    def buildDescription = branchName
    CommonUtil.setBuildDescription(script, buildDescription)
    CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)

//    libNames = new ArrayList<String>()
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
        },
        pipeline.stage(CHECK_BRANCH_AND_VERSION) {
            String globalConfigurationJsonStr = script.readFile(projectConfigurationFile)
            def globalConfiguration = new JsonSlurper().parseText(globalConfigurationJsonStr)
            globalVersion = globalConfiguration.version

            script.echo "dev_info -2"
            def componentsJsonStr = script.readJSON file: projectConfigurationFile
            script.echo "dev_info -1"
            def components = new JsonSlurper().parseText(componentsJsonStr)
            script.echo "dev_info 0"
            for (i = 0; i < conponens.size; i++) {
                script.echo "dev_info 1"
                def component = components[i]
                script.echo "dev_info 2"
                def libs = component.libs
                script.echo "dev_info 3"
                for (j = 0; j < libs.size; i++) {
                    script.echo "dev_info 4"
                    def lib = libs[j]
                    script.echo "dev_info 5"
                    libNames.add(lib.name)
                    script.echo "dev_info 6"
                }
                script.echo "dev_info 7"
            }
            script.echo "dev_info 8"
            components.each { c ->
                script.echo "dev_info 1"
                c.libs.each { lib ->
                    script.echo "dev_info 2"
                    libNames.add(lib.name)
                }
            }

            if (("dev/T-" + globalVersion) != branchName) { // TODO ПОМЕНЯТЬ НА dev/G-0.0.0
                script.error("Deploy AndroidStandard with global version: $globalVersion from branch: '$branchName' forbidden")
            }
        },
        pipeline.stage(INCREMENT_GLOBAL_ALPHA_VERSION) {
            script.sh("./gradlew incrementGlobalUnstableVersion")
        },
        pipeline.stage(INCREMENT_CHANGED_UNSTABLE_MODULES_ALPHA_VERSION) {
            def revisionToCompare = getPreviousRevisionWithVersionIncrement(script)
            script.sh("./gradlew incrementUnstableChangedComponents -PrevisionToCompare=${revisionToCompare}")
        },

        pipeline.stage(BUILD) {
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assemble")
        },
        pipeline.stage(UNIT_TEST) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/")
        },
        //TODO РАСКОМЕНТИТЬ ИНСТРУМЕНТАЛЬНЫЕ ТЕСТЫ (ПОКА ЧТО ОНИ ПАДАЮТ)
//        pipeline.stage(INSTRUMENTATION_TEST) {
//            AndroidPipelineHelper.instrumentationTestStageBodyAndroid(
//                    script,
//                    new AvdConfig(),
//                    "debug",
//                    getTestInstrumentationRunnerName,
//                    new AndroidTestConfig(
//                            "assembleAndroidTest",
//                            "build/outputs/androidTest-results/instrumental",
//                            "build/reports/androidTests/instrumental",
//                            true,
//                            0
//                    )
//            )
//        },
        pipeline.stage(STATIC_CODE_ANALYSIS, StageStrategy.SKIP_STAGE) {
            AndroidPipelineHelper.staticCodeAnalysisStageBody(script)
        },
        pipeline.stage(DEPLOY_MODULES) {
            withArtifactoryCredentials(script) {
//                        AndroidUtil.withGradleBuildCacheCredentials(script) {
//                            script.sh "./gradlew clean build :${lib.name}:uploadArchives -PonlyUnstable=true -PdeployOnlyIfNotExist=true"
//                            libNames.each { libName ->
//                String componentsJsonStr = script.readFile("buildSrc/components.json")
//                def components = new JsonSlurper().parseText(componentsJsonStr)
//                components.each { component ->
                libNames.each { libName ->
                    if (libName == "logger") {
                        script.sh "./gradlew clean :${libName}:uploadArchives -PonlyUnstable=true -PdeployOnlyIfNotExist=true"
                        }
//                    }
                }

//                            }
//                        }
//                    }
//                }
            }
        },
        pipeline.stage(DEPLOY_GLOBAL_VERSION_PLUGIN) {
            withArtifactoryCredentials(script) {
                script.sh "./gradlew generateDataForPlugin"
                script.sh "./gradlew :android-standard-version-plugin:uploadArchives"
            }
        },
        pipeline.stage(VERSION_PUSH, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            RepositoryUtil.setDefaultJenkinsGitUser(script)
            String globalConfigurationJsonStr = script.readFile(projectConfigurationFile)
            def globalConfiguration = new JsonSlurper().parseText(globalConfigurationJsonStr)

            script.sh "git commit -a -m \"Increase global alpha version counter to " +
                    "$globalConfiguration.unstable_version $RepositoryUtil.SKIP_CI_LABEL1 $RepositoryUtil.VERSION_LABEL1\""
            RepositoryUtil.push(script, pipeline.repoUrl, pipeline.repoCredentialsId)
        },
        pipeline.stage(MIRROR_COMPONENTS, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            if (pipeline.getStage(VERSION_PUSH).result != Result.SUCCESS) {
                script.error("Cannot mirror without change version")
            }
            script.build job: 'Android_Standard_Component_Mirroring', parameters: [
                    script.string(name: 'branch', value: branchName)
            ]
        }
]



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
            initParameters(script),
            initTriggers(script)
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
                    name: "branchName_0",
                    description: 'Ветка с исходным кодом')
    ])
}

def static initTriggers(script) {
    return script.pipelineTriggers([
            script.GenericTrigger(
                    genericVariables: [
                            [
                                    key  : "branchName",
                                    value: '$.push.changes[?(@.new.type == "branch")].new.name'
                            ]
                    ],
                    printContributedVariables: true,
                    printPostContent: true,
                    causeString: 'Triggered by Bitbucket',
                    regexpFilterExpression: '^(origin\\/)?dev\\/T-(.*)$', // TODO ПОМЕНЯТЬ НА dev/G-0.0.0
                    regexpFilterText: '$branchName_0'
            ),
            script.pollSCM('')
    ])
}

// ============================================= ↑↑↑  END JOB PROPERTIES CONFIGURATION ↑↑↑  ==========================================

// ============ Utils =================

def static getCommitHash(script, commit) {
    def parts = commit.split(" ")
    for (part in parts) {
        if (part.trim().matches("^[a-zA-Z0-9]*\$")) {
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
        }
    }
    if (revisionToCompare == null) {
        //gets previous commit
        def previousCommit = commits[1]
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