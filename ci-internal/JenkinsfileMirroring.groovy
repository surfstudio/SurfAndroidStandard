@Library('surf-lib@version-1.0.0-SNAPSHOT') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.PrPipelineAndroid
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.stage.StageStrategy

//init
def pipeline = new EmptyScmPipeline(this)

//configuration
def mirrorRepoUrl = "https://github.com/surfstudio/SurfAndroidStandard.git"
def mirrorRepoCredentialID = "76dbac13-e6ea-4ed0-a013-e06cad01be2d"

//stages
pipeline.stages = [
        pipeline.createStage("MIRRORING", StageStrategy.FAIL_WHEN_STAGE_ERROR) {
            //todo
        }
]

//run
pipeline.run()