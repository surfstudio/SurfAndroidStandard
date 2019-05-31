package ru.surfstudio.android.build

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication

object Artifactory {

    private const val ARTIFACTORY_URL = "https://artifactory.surfstudio.ru/artifactory"
    private const val LIB_URL = "/libs-release-local"
    private const val DISTRIBUTE_URL = "$ARTIFACTORY_URL/api/distribute"
    private const val BITRAY_URL = "https://dl.bintray.com/surf/maven"
    //    private const val BITRAY_URL = "https://bintray.com/surf/maven"
    const val DEPLOY_URL = ARTIFACTORY_URL + LIB_URL

    //    val userName = System.getenv("surf_maven_username")
    val userName = "trushchinskii"
    //    val password = System.getenv("surf_maven_password")
    val password = "traktorvpoletru"

    /**
     * Deploy artifacts to bitray
     */
    @JvmStatic
    fun distributeArtifactsToBitray() {

        val bodyJson = """
        {
          "publish" : true,
          "overrideExistingFiles" : false,
          "async" : false,
          "targetRepo" : "$BITRAY_URL",
          "packagesRepoPaths" : ["${LIB_URL}/com/example/analytics/"],
          "dryRun" : false
        }
        """
        Fuel.post(DISTRIBUTE_URL)
                .header("Content-Type" to "application/json")
                .body(bodyJson)
                .authentication()
                .basic(userName, password)
                .response { request, response, result ->
                    println("123123 request : $request")
                    println("123123 response : $response")
                    println("123123 result : $result")
                }
    }
}