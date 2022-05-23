@Library('surf-lib@version-4.1.3-SNAPSHOT')
// https://github.com/surfstudio/jenkins-pipeline-lib
import groovy.json.JsonSlurperClassic
import ru.surfstudio.ci.*
import ru.surfstudio.ci.pipeline.ScmPipeline
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.utils.android.AndroidUtil
import ru.surfstudio.ci.utils.android.config.AndroidTestConfig
import ru.surfstudio.ci.utils.android.config.AvdConfig
import ru.surfstudio.ci.utils.buildsystems.GradleUtil

//Pipeline for deploy snapshot artifacts

// Stage names
def CHECKOUT = 'Checkout'
def NOTIFY_ABOUT_NEW_RELEASE_NOTES = 'Notify About New Release Notes'
def CHECK_BRANCH_AND_VERSION = 'Check Branch & Version'
def CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT = 'Check Configuration Is Not Project Snapshot'
def INCREMENT_GLOBAL_ALPHA_VERSION = 'Increment Global Alpha Version'
def INCREMENT_CHANGED_UNSTABLE_MODULES_ALPHA_VERSION = 'Increment Changed Unstable Modules Alpha Version'
def BUILD = 'Build'
def BUILD_TEMPLATE = 'Template Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'
def UPDATE_TEMPLATE_VERSION_PLUGIN = 'Update Template Version Plugin'
def DEPLOY_MODULES = 'Deploy Modules'
def DEPLOY_GLOBAL_VERSION_PLUGIN = 'Deploy Global Version Plugin'
def VERSION_PUSH = 'Version Push'
def MIRROR_COMPONENTS = 'Mirror Components'

//constants
def projectConfigurationFile = "buildSrc/projectConfiguration.json"
def androidStandardTemplateConfigurationFile = "template/config.gradle"
def projectConfigurationVersionFile = "buildSrc/build/tmp/projectVersion.txt"
def releaseNotesChangesFileUrl = "buildSrc/build/tmp/releaseNotesChanges.txt"
def idChatAndroidSlack = "CFSF53SJ1"

//vars
def branchName = ""
def globalVersion = "<unknown>"
def buildDescription = ""
def useJava11 = true

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
    if (stage.name != CHECKOUT) RepositoryUtil.notifyGithubAboutStageStart(script, pipeline.repoUrl, stage.name)
}
pipeline.postExecuteStageBody = { stage ->
    if (stage.name != CHECKOUT) RepositoryUtil.notifyGithubAboutStageFinish(script, pipeline.repoUrl, stage.name, stage.result)
}

pipeline.initializeBody = {
    CommonUtil.printInitialStageStrategies(pipeline)

    script.echo "artifactory user: ${script.env.surf_maven_username}"

    //Выбираем значения веток из параметров, Установка их в параметры происходит
    // если триггером был webhook или если стартанули Job вручную
    //Используется имя branchName_0 из за особенностей jsonPath в GenericWebhook plugin
    CommonUtil.extractValueFromEnvOrParamsAndRun(script, 'branchName') {
        value -> branchName = value
    }

    branchName = branchName.replace("refs/heads/", "")
    if (branchName.contains("origin/")) {
        branchName = branchName.replace("origin/", "")
    }

    buildDescription = branchName
    CommonUtil.setBuildDescription(script, buildDescription)
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
                scmSkip(deleteBuild: true, skipPattern: '.*\\[skip ci\\].*')
            }
            CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)

            RepositoryUtil.saveCurrentGitCommitHash(script)
            RepositoryUtil.checkLastCommitMessageContainsSkipCiLabel(script)
        },
        pipeline.stage(NOTIFY_ABOUT_NEW_RELEASE_NOTES, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR, false) {
            def commitParents = script.sh(returnStdout: true, script: 'git log -1  --pretty=%P').split(' ')
            def prevCommitHash = commitParents[0]
            GradleUtil.gradlew(script, "WriteToFileReleaseNotesDiffForSlack -PrevisionToCompare=${prevCommitHash}", useJava11)
            String releaseNotesChanges = script.readFile(releaseNotesChangesFileUrl)
            if (releaseNotesChanges.trim() != "") {
                releaseNotesChanges = "Android Standard changes:\n$releaseNotesChanges"
                JarvisUtil.sendMessageToGroup(script, releaseNotesChanges, idChatAndroidSlack, "slack", true)
            }
        },
        pipeline.stage(CHECK_BRANCH_AND_VERSION) {
            def globalConfiguration = getGlobalConfiguration(script, projectConfigurationFile)
            globalVersion = globalConfiguration.version

            if (("dev/G-" + globalVersion) != branchName) {
                script.error("Deploy AndroidStandard with global version: dev/G-${globalVersion} from branch: '$branchName' forbidden")
            }
        },
        pipeline.stage(CHECK_CONFIGURATION_IS_NOT_PROJECT_SNAPSHOT) {
            GradleUtil.gradlew(script, "checkConfigurationIsNotProjectSnapshotTask", useJava11)
        },
        pipeline.stage(INCREMENT_GLOBAL_ALPHA_VERSION) {
            GradleUtil.gradlew(script, "incrementGlobalUnstableVersion", useJava11)
        },
        pipeline.stage(UPDATE_TEMPLATE_VERSION_PLUGIN) {
            GradleUtil.gradlew(script, "generateProjectConfigurationVersionFileTask", useJava11)

            def currentStandardVersion = script.readFile(projectConfigurationVersionFile)

            AndroidUtil.changeGradleVariable(
                    script,
                    androidStandardTemplateConfigurationFile,
                    "androidStandardVersion",
                    "'$currentStandardVersion'"
            )
        },
        pipeline.stage(INCREMENT_CHANGED_UNSTABLE_MODULES_ALPHA_VERSION, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            def revisionToCompare = getPreviousRevisionWithVersionIncrement(script)
            GradleUtil.gradlew(script, "incrementUnstableChangedComponents -PrevisionToCompare=${revisionToCompare}", useJava11)
        },
        pipeline.stage(BUILD) {
            script.sh("rm -rf temp template/**/build")
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assembleRelease", useJava11)
        },
        pipeline.stage(UNIT_TEST) {
            AndroidPipelineHelper.unitTestStageBodyAndroid(
                    script,
                    "testReleaseUnitTest",
                    "**/test-results/testReleaseUnitTest/*.xml",
                    "app/build/reports/tests/testReleaseUnitTest/",
                    useJava11
            )
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
        },
        pipeline.stage(DEPLOY_MODULES) {
            withJobCredentials(script) {
                GradleUtil.gradlew(script, "clean publish -PonlyUnstable=true -PdeployOnlyIfNotExist=true -PpublishType=artifactory", useJava11)
            }
        },
        pipeline.stage(DEPLOY_GLOBAL_VERSION_PLUGIN) {
            withJobCredentials(script) {
                GradleUtil.gradlew(script, "generateDataForPlugin", useJava11)
                GradleUtil.gradlew(script, ":android-standard-version-plugin:publish", useJava11)
            }
        },
        pipeline.stage(BUILD_TEMPLATE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("echo \"androidStandardDebugDir=$workspace\n" +
                    "androidStandardDebugMode=false\n" +
                    "skipSamplesBuild=true\" > template/android-standard/androidStandard.properties")
            // build template after deploy in order to check usage of new artifacts
            GradleUtil.gradlew(script, "-p template :app:dependencies", useJava11)
            AndroidPipelineHelper.buildStageBodyAndroid(script, "-p template clean assembleQa assembleRelease --stacktrace", useJava11)
        },
        pipeline.stage(VERSION_PUSH, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            RepositoryUtil.setDefaultJenkinsGitUser(script)
            def globalConfiguration = getGlobalConfiguration(script, projectConfigurationFile)

            script.sh "git commit -a -m \"Increase global alpha version counter to " +
                    "$globalConfiguration.unstable_version $RepositoryUtil.SKIP_CI_LABEL1 $RepositoryUtil.VERSION_LABEL1\""
            RepositoryUtil.push(script, pipeline.repoUrl, pipeline.repoCredentialsId)
        },
        pipeline.stage(MIRROR_COMPONENTS, StageStrategy.SKIP_STAGE) {
            if (pipeline.getStage(VERSION_PUSH).result != Result.SUCCESS) {
                script.error("Cannot mirror without change version")
            }
            script.build job: 'Android_Standard_Component_Mirroring_Job', parameters: [
                    script.string(name: 'branch', value: branchName),
                    script.string(name: 'lastCommit', value: getPreviousRevisionWithVersionIncrement(script))
            ]
        }
]


pipeline.finalizeBody = {
    def jenkinsLink = CommonUtil.getBuildUrlSlackLink(script)
    def message
    def success = Result.SUCCESS == pipeline.jobResult
    def unstable = Result.UNSTABLE == pipeline.jobResult
    def checkoutAborted = pipeline.getStage(CHECKOUT).result == Result.ABORTED
    if (!checkoutAborted) {
        if (!success) {
            def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
            if (unstable) {
                message = "Deploy из ветки '${branchName}' выполнен. Найдены нестабильные этапы: ${unsuccessReasons}. ${jenkinsLink}"
            } else {
                message = "Deploy из ветки '${branchName}' не выполнен из-за этапов: ${unsuccessReasons}. ${jenkinsLink}"
            }
        } else {
            message = "Deploy из ветки '${branchName}' успешно выполнен. ${jenkinsLink}"
        }

        JarvisUtil.sendMessageToGroup(script, message, pipeline.repoUrl, "github", pipeline.jobResult)
    }
}

pipeline.run()

// ============================================= ↓↓↓ JOB PROPERTIES CONFIGURATION ↓↓↓  ==========================================

static List<Object> initProperties(ScmPipeline ctx) {
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
                    numToKeepStr: '200'
            )
    )
}

def static initParameters(script) {
    return script.parameters([
            script.string(
                    name: "branchName",
                    description: 'Ветка с исходным кодом'
            )
    ])
}

def static initTriggers(script) {
    return script.pipelineTriggers([
            script.GenericTrigger(
                    genericVariables: [
                            [
                                    key  : "branchName",
                                    value: '$.ref'
                            ]
                    ],
                    printContributedVariables: true,
                    printPostContent: true,
                    causeString: 'Triggered by Github',
                    regexpFilterExpression: '^(origin\\/)?refs\\/heads\\/dev\\/G-(.*)$',
                    regexpFilterText: '$branchName'
            ),
            script.pollSCM('')
    ])
}

// ============================================== ↑↑↑  END JOB PROPERTIES CONFIGURATION ↑↑↑  ==========================================

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
            script: "git  --no-pager log --pretty=oneline -500 --graph"
    )
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

def static withJobCredentials(script, body) {
    script.withCredentials([
            script.file(
                    credentialsId: "surf_maven_sign_key_ring_file",
                    variable: 'surf_maven_sign_key_ring_file'
            ),
            script.usernamePassword(
                    credentialsId: "Maven_Sign_Credential",
                    usernameVariable: 'surf_maven_sign_key_id',
                    passwordVariable: 'surf_maven_sign_password'
            ),
            script.usernamePassword(
                    credentialsId: "Artifactory_Deploy_Credentials",
                    usernameVariable: 'surf_maven_username',
                    passwordVariable: 'surf_maven_password'
            )
    ]) {
        body()
    }
}

def static getGlobalConfiguration(script, projectConfigurationFile) {
    String globalConfigurationJsonStr = script.readFile(projectConfigurationFile)
    return new JsonSlurperClassic().parseText(globalConfigurationJsonStr)
}
