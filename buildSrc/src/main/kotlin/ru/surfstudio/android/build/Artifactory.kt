package ru.surfstudio.android.build

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.isSuccessful
import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.ArtifactInfo

object Artifactory {

    private const val ARTIFACTORY_URL = "https://artifactory.surfstudio.ru/artifactory"
    private const val DISTRIBUTE_URL = "$ARTIFACTORY_URL/api/distribute"
    private const val TARGET_REPO = "libs-release-remote"
    const val SOURCE_REPO = "libs-release-local"

    const val DEPLOY_URL = "$ARTIFACTORY_URL/$SOURCE_REPO"
    const val ANDROID_STANDARD_GROUP_ID = "ru.surfstudio.android"

    val userName = System.getenv("surf_maven_username")
    val password = System.getenv("surf_maven_password")

    /**
     * Deploy artifact to bintray
     */
    @JvmStatic
    fun distributeArtifactToBintray(vararg libraryNames: String) {
        val artifacts: List<ArtifactInfo> = libraryNames.map { ArtifactInfo(it) }

        var packagesRepoPaths = ""
        artifacts.forEachIndexed { index, artifactInfo ->
            packagesRepoPaths += artifactInfo.getPath()
            if (index != artifacts.size - 1) packagesRepoPaths += ", "
        }

        val bodyJson = """
            {
              "targetRepo" : "$TARGET_REPO",
              "packagesRepoPaths" : [$packagesRepoPaths]
            }
        """
        val response = Fuel.post(DISTRIBUTE_URL)
                .header("Content-Type" to "application/json")
                .body(bodyJson)
                .authentication()
                .basic(userName, password)
                .response().second
        if (!response.isSuccessful) throw GradleException(response.toString())
    }
}