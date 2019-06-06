package ru.surfstudio.android.build.artifactory

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.isSuccessful
import com.google.gson.GsonBuilder
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig.ANDROID_STANDARD_GROUP_ID
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig.DISTRIBUTE_URL
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig.GET_FOLDER_INFO
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig.PASSWORD
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig.TARGET_REPO
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig.USER_NAME
import ru.surfstudio.android.build.exceptions.FolderNotFoundException
import ru.surfstudio.android.build.model.FolderInfo
import ru.surfstudio.android.build.model.json.response.folder_info.FolderInfoJson

/**
 * Class to use artifactory api
 */
internal class ArtifactoryRepository {

    private val gson = GsonBuilder().create()

    /**
     * Get folder info
     */
    fun getFolderInfo(folderPath: String): FolderInfo {
        val groupIdPath: String = ANDROID_STANDARD_GROUP_ID.replace(".", "/")

        val response = Fuel.get("$GET_FOLDER_INFO/$groupIdPath/$folderPath")
                .response()
                .second

        if (response.isSuccessful) {
            val data = String(response.data)

            return gson.fromJson(data, FolderInfoJson::class.java).transform()
        } else {
            throw FolderNotFoundException(folderPath)
        }
    }

    /**
     * Deploy artifact to bintray
     */
    fun distribute(packagesRepoPaths: String, overrideExisting: Boolean): Response {
        val bodyJson = """
            {
              "overrideExistingFiles" : "${overrideExisting.toString().toLowerCase()}",
              "targetRepo" : "$TARGET_REPO",
              "packagesRepoPaths" : [$packagesRepoPaths]
            }
        """

        return Fuel.post(DISTRIBUTE_URL)
                .header("Content-Type" to "application/json")
                .body(bodyJson)
                .authentication()
                .basic(USER_NAME, PASSWORD)
                .response()
                .second
    }
}