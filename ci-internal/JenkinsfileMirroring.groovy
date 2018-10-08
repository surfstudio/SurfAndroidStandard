@Library('surf-lib@version-1.0.0-SNAPSHOT') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.NodeProvider
import java.net.URLEncoder

def encodeUrl(string){
    URLEncoder.encode(string, "UTF-8") 
}

//init
def pipeline = new EmptyScmPipeline(this)

//configuration
def mirrorRepoCredentialID = "76dbac13-e6ea-4ed0-a013-e06cad01be2d"

pipeline.node = NodeProvider.getAndroidNode()

pipeline.propertiesProvider = {
    return [
        pipelineTriggers([
            GenericTrigger(),
            pollSCM('')
        ])
    ]
}

//stages
pipeline.stages = [
        pipeline.createStage("Clone", StageStrategy.FAIL_WHEN_STAGE_ERROR) {
            sh "rm -rf android-standard.git"
            withCredentials([usernamePassword(credentialsId: pipeline.repoCredentialsId, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                echo "credentialsId: $pipeline.repoCredentialsId"
                sh "git clone --mirror https://${encodeUrl(USERNAME)}:${encodeUrl(PASSWORD)}@bitbucket.org/surfstudio/android-standard.git"
            }
        },
        pipeline.createStage("Mirroing", StageStrategy.FAIL_WHEN_STAGE_ERROR) {
            dir("android-standard.git") {
                withCredentials([usernamePassword(credentialsId: mirrorRepoCredentialID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    echo "credentialsId: $mirrorRepoCredentialID"
                    sh "git push --mirror https://${encodeUrl(USERNAME)}:${encodeUrl(PASSWORD)}@github.com/surfstudio/SurfAndroidStandard.git"
                }
            }
        }
]

//run
pipeline.run()