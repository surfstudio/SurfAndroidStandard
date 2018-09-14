@Library('surf-lib@version-1.0.0-SNAPSHOT') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.pipeline.helper.AndroidPipelineHelper
import ru.surfstudio.ci.JarvisUtil
import ru.surfstudio.ci.CommonUtil
import ru.surfstudio.ci.RepositoryUtil
import ru.surfstudio.ci.NodeProvider
import ru.surfstudio.ci.Result
import ru.surfstudio.ci.AbortDuplicateStrategy

import static ru.surfstudio.ci.CommonUtil.applyParameterIfNotEmpty

//Кастомный пайплайн для деплоя артефактов

// Имена Шагов
def CHECKOUT = 'Checkout'
def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'
def DEPLOY = 'Deploy'

//init
def script = this
def pipeline = new EmptyScmPipeline(script)
def branchName = ""
pipeline.init()

//configuration
pipeline.node = NodeProvider.getAndroidNode()

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
    applyParameterIfNotEmpty(script, 'branchName', script.params.branchName_0, {
        value -> branchName = value
    })

    if(branchName.contains("project-snapshot")){ //todo do not ignore stages fore project-release build (нжно парсить config файл и смотреть на постфикс SNAPSHOT)
        script.echo "Apply lightweight strategies for project-snapshot branch"
        pipeline.getStage(BUILD).strategy = StageStrategy.SKIP_STAGE
        pipeline.getStage(UNIT_TEST).strategy = StageStrategy.SKIP_STAGE
        pipeline.getStage(INSTRUMENTATION_TEST).strategy = StageStrategy.SKIP_STAGE
        pipeline.getStage(STATIC_CODE_ANALYSIS).strategy = StageStrategy.SKIP_STAGE
    }

    CommonUtil.safe(script){
        JarvisUtil.sendMessageToGroup(script, "Инициирован Deploy ветки ${branchName}", pipeline.repoUrl, "bitbucket", true)
    }

    def buildDescription = branchName
    CommonUtil.setBuildDescription(script, buildDescription)
    CommonUtil.abortDuplicateBuildsWithDescription(script, AbortDuplicateStrategy.ANOTHER, buildDescription)
}

pipeline.stages = [
        pipeline.createStage(CHECKOUT, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            script.checkout([
                    $class                           : 'GitSCM',
                    branches                         : [[name: "${branchName}"]],
                    doGenerateSubmoduleConfigurations: script.scm.doGenerateSubmoduleConfigurations,
                    userRemoteConfigs                : script.scm.userRemoteConfigs,
            ])
            RepositoryUtil.saveCurrentGitCommitHash(script)
        },
        pipeline.createStage(BUILD, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            AndroidPipelineHelper.buildStageBodyAndroid(script, "clean assemble")
        },
        pipeline.createStage(UNIT_TEST, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            AndroidPipelineHelper.unitTestStageBodyAndroid(script,
                            "testReleaseUnitTest",
                            "**/test-results/testReleaseUnitTest/*.xml",
                            "app/build/reports/tests/testReleaseUnitTest/")
        },
        pipeline.createStage(INSTRUMENTATION_TEST, StageStrategy.SKIP_STAGE) {
            AndroidPipelineHelper.instrumentationTestStageBodyAndroid(script,
                    "connectedAndroidTest",
                    "**/outputs/androidTest-results/connected/*.xml",
                    "app/build/reports/androidTests/connected/")
        },
        pipeline.createStage(STATIC_CODE_ANALYSIS, StageStrategy.SKIP_STAGE) {
            AndroidPipelineHelper.staticCodeAnalysisStageBody(script)
        },
        pipeline.createStage(DEPLOY, StageStrategy.FAIL_WHEN_STAGE_ERROR) {
            script.sh "./gradlew clean uploadArchives"
        }
]

pipeline.finalizeBody = {
    def jenkinsLink = CommonUtil.getBuildUrlMarkdownLink(script)
    def message
    def success = Result.SUCCESS.equals(pipeline.jobResult)
    if (!success) {
        def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
        message = "Deploy ветки ${branchName} не выполнен из-за этапов: ${unsuccessReasons}. ${jenkinsLink}"
    } else {
        message = "Deploy ветки ${branchName} успешно выполнен. ${jenkinsLink}"
    }
    JarvisUtil.sendMessageToGroup(script, message, pipeline.repoUrl, "bitbucket", success)
}

pipeline.run()
