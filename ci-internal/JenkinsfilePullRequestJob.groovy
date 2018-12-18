@Library('surf-lib@android-instrumental-tests') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.pr.PrPipelineAndroid
import ru.surfstudio.ci.stage.StageStrategy

//init
def pipeline = new PrPipelineAndroid(this)
pipeline.init()

//customization
pipeline.getStage(pipeline.UNIT_TEST).strategy = StageStrategy.SKIP_STAGE
pipeline.getStage(pipeline.INSTRUMENTATION_TEST).strategy = StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
pipeline.getStage(pipeline.STATIC_CODE_ANALYSIS).strategy = StageStrategy.SKIP_STAGE
pipeline.buildGradleTask = "clean \
        :template:app-injector:assembleDebug \
        :network-sample:assembleDebug \
        :custom-view-sample:assembleDebug"
pipeline.androidTestConfig.testBuildType = "debug"

pipeline.preExecuteStageBody = {}
pipeline.postExecuteStageBody = {}

pipeline.finalizeBody = {}

//run
pipeline.run()
