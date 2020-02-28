@Library('surf-lib@version-2.0.0-SNAPSHOT')
// https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.tag.TagPipelineAndroid
import ru.surfstudio.ci.stage.StageStrategy

//init
def pipeline = new TagPipelineAndroid(this)
pipeline.init()

//configuration
pipeline.keystoreCredentials = null
pipeline.keystorePropertiesCredentials = null

pipeline.useFirebaseDistribution = true

//customization
pipeline.getStage(pipeline.INSTRUMENTATION_TEST).strategy = StageStrategy.SKIP_STAGE
pipeline.getStage(pipeline.STATIC_CODE_ANALYSIS).strategy = StageStrategy.SKIP_STAGE

//run
pipeline.run()