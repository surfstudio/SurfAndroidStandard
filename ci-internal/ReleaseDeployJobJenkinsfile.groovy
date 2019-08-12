@Library('surf-lib@version-2.0.0-SNAPSHOT')
// https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import groovy.json.JsonSlurperClassic
import ru.surfstudio.ci.JarvisUtil
import ru.surfstudio.ci.CommonUtil
import ru.surfstudio.ci.pipeline.ScmPipeline
import ru.surfstudio.ci.RepositoryUtil
import ru.surfstudio.ci.utils.android.AndroidUtil
import ru.surfstudio.ci.Result
import ru.surfstudio.ci.AbortDuplicateStrategy
import ru.surfstudio.ci.utils.android.config.AndroidTestConfig
import ru.surfstudio.ci.utils.android.config.AvdConfig
import java.util.regex.Pattern
import java.util.regex.Matcher

import static ru.surfstudio.ci.CommonUtil.applyParameterIfNotEmpty

//Pipeline for deploy snapshot artifacts

// Stage names

def CHECKOUT = 'Checkout'
def CHECK_BRANCH_AND_VERSION = 'Check Branch & Version'
def CHECK_COMPONENT_DEPENDENCY_STABLE = 'Check Component Dependency Stable'
def CHECK_COMPONENT_DEPENDENCY_IN_ARTIFACTORY = 'Check Component Dependency In Artifactory'
def CHECK_COMPONENT_ALREADY_IN_ARTIFACTORY = 'Check Component Already In Artifactory'
def CHECK_COMPONENT_STABLE = 'Check Component Stable'
def CHECK_COMPONENTS_DEPENDENT_FROM_CURRENT_UNSTABLE = 'Check Components Dependent From Current Unstable'
def CHECK_RELEASE_NOTES_VALID = 'Check Release Notes Valid'
def CHECKS_RESULT = 'Checks Result'
def SET_COMPONENT_ALPHA_COUNTER_TO_ZERO = "Set Component Alpha Counter To Zero"

def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'
def DEPLOY_MODULES = 'Deploy Modules'
def COMPONENT_ALPHA_COUNTER_PUSH = 'Component Alpha Counter Push'
def MIRROR_COMPONENT = 'Mirror Components'

//constants
def projectConfigurationFile = "buildSrc/projectConfiguration.json"
def componentsFile = "buildSrc/components.json"

//vars
def branchName = ""
def componentVersion = "<unknown>"
def componentName = "<unknown>"


def p1 = "deploySameVersionArtifactory"

def p2 = "deploySameVersionBintray" //todo

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
pipeline.propertiesProvider = { properties(pipeline) }

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
            //release_<component>_<version>
            def parts = branchName.split("_")
            componentVersion = parts[2]
            componentName = parts[1]
            script.sh("./gradlew checkVersionEqualsComponentVersion -Pcomponent=${componentName} -PcomponentVersion=${componentVersion}")
        },
        pipeline.stage(CHECK_COMPONENT_DEPENDENCY_STABLE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkStandardDependenciesStableTask -Pcomponent=${componentName}")
        },
        pipeline.stage(CHECK_COMPONENT_DEPENDENCY_IN_ARTIFACTORY, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            withArtifactoryCredentials(script) {
                script.sh("./gradlew checkExistingDependencyArtifactsInArtifactory -Pcomponent=${componentName}")
                script.sh("./gradlew checkExistingDependencyArtifactsInBintray -Pcomponent=${componentName}")
            }
        },
        pipeline.stage(CHECK_COMPONENT_ALREADY_IN_ARTIFACTORY, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            withArtifactoryCredentials(script) {
                script.sh("./gradlew checkSameArtifactsInArtifactory -Pcomponent=${componentName}")
                script.sh("./gradlew checkSameArtifactsInBintray -Pcomponent=${componentName}")
            }
        },
        pipeline.stage(CHECK_COMPONENT_STABLE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkComponentStable -Pcomponent=${componentName}")
        },
        pipeline.stage(CHECK_COMPONENTS_DEPENDENT_FROM_CURRENT_UNSTABLE, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkDependencyForComponentUnstable -Pcomponent=${componentName}")
        },
        pipeline.stage(CHECK_RELEASE_NOTES_VALID, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            script.sh("./gradlew checkReleaseNotesContainCurrentVersion")
        },
        pipeline.stage(CHECKS_RESULT) {
            def checksPassed = true
            [
                    CHECK_COMPONENT_DEPENDENCY_STABLE,
                    CHECK_COMPONENT_DEPENDENCY_IN_ARTIFACTORY,
                    CHECK_COMPONENT_ALREADY_IN_ARTIFACTORY,
                    CHECK_COMPONENT_STABLE,
                    CHECK_COMPONENTS_DEPENDENT_FROM_CURRENT_UNSTABLE,
                    CHECK_RELEASE_NOTES_VALID
            ].forEach {stageName ->
                def stageResult = pipeline.getStage(stageName).result
                checksPassed = checksPassed && (stageResult == Result.SUCCESS || stageResult == Result.NOT_BUILT)
            }

            if(!checksPassed) {
                script.error("Checks Failed")
            }
        },
        pipeline.stage(SET_COMPONENT_ALPHA_COUNTER_TO_ZERO) {
            script.sh("./gradlew setComponentAlphaCounterToZero -PrevisionToCompare=${revisionToCompare}")
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
        },
        pipeline.stage(DEPLOY_MODULES) {
            withArtifactoryCredentials(script) {
                AndroidUtil.withGradleBuildCacheCredentials(script) {
                    script.sh "./gradlew clean uploadArchives" //todo указать нужный артефакт
                }
            }
        },
        pipeline.stage(COMPONENT_ALPHA_COUNTER_PUSH, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            //todo переписать логику на пуш обнуления каунтера
            RepositoryUtil.setDefaultJenkinsGitUser(script)
            String globalConfigurationJsonStr = script.readFile(projectConfigurationFile)
            def globalConfiguration = new JsonSlurperClassic().parseText(globalConfigurationJsonStr)

            script.sh "git commit -a -m \"Increase global alpha version counter to " +
                    "$globalConfiguration.unstable_version $RepositoryUtil.SKIP_CI_LABEL1 $RepositoryUtil.VERSION_LABEL1\""
            RepositoryUtil.push(script, pipeline.repoUrl, pipeline.repoCredentialsId)
        },
        pipeline.stage(MIRROR_COMPONENT, StageStrategy.UNSTABLE_WHEN_STAGE_ERROR) {
            if (pipeline.getStage(VERSION_PUSH).result != Result.SUCCESS) {
                script.error("Cannot mirror without change version")
            }
            script.build job: 'Android_Standard_Component_Mirroring', parameters: [
                    string(name: 'branch', value: branchName)
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


def static List<Object> properties(ScmPipeline ctx) {
    def script = ctx.script
    return [
            buildDiscarder(script),
            parameters(script),
            triggers(script)
    ]
}

def static buildDiscarder(script) {
    return script.buildDiscarder(
            script.logRotator(
                    artifactDaysToKeepStr: '3',
                    artifactNumToKeepStr: '10',
                    daysToKeepStr: '60',
                    numToKeepStr: '200')
    )
}

def static parameters(script) {
    return script.parameters([
            script.string(
                    name: "branchName_0",
                    description: 'Ветка с исходным кодом')
    ])
}

def static triggers(script) {
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
                    regexpFilterExpression: '^(origin\\/)?release-(.*)$', //todo изменить фильтр веток
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
        if (commit.startWith("*")) {
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
