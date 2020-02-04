@Library('surf-lib@version-2.0.0-SNAPSHOT')
// https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.pr.PrPipelineAndroid
import ru.surfstudio.ci.stage.StageStrategy

//init
def pipeline = new PrPipelineAndroid(this)
pipeline.init()

def qa = "Qa"
def testSources = "AndroidTestSources"

//customization
pipeline.getStage(pipeline.CODE_STYLE_FORMATTING).strategy = StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
pipeline.getStage(pipeline.UPDATE_CURRENT_COMMIT_HASH_AFTER_FORMAT).strategy = StageStrategy.UNSTABLE_WHEN_STAGE_ERROR

pipeline.stagesForTargetBranchChangedMode = [pipeline.PRE_MERGE, pipeline.BUILD]

pipeline.buildGradleTask = "clean assemble$qa :app:compile$qa$testSources"

//run
pipeline.run()