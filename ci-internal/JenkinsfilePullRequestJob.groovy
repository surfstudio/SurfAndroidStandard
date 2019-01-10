@Library('surf-lib@version-1.0.0-SNAPSHOT') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.pr.PrPipelineAndroid
import ru.surfstudio.ci.stage.StageStrategy

//init
def pipeline = new PrPipelineAndroid(this)
pipeline.init()

//customization
pipeline.node = "android-2"
pipeline.getStage(pipeline.STATIC_CODE_ANALYSIS).strategy = StageStrategy.SKIP_STAGE
pipeline.buildGradleTask = "clean assembleRelease"
pipeline.androidTestBuildType = "release"

//run
pipeline.run()
