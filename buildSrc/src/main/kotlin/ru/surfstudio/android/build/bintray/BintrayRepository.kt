package ru.surfstudio.android.build.bintray

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.isSuccessful
import com.google.gson.GsonBuilder
import org.gradle.internal.impldep.com.google.api.client.http.HttpStatusCodes
import ru.surfstudio.android.build.exceptions.UnableGetBintrayPackagesException
import ru.surfstudio.android.build.exceptions.UnauthorizedException
import ru.surfstudio.android.build.model.BintrayRepoLatestVersion
import ru.surfstudio.android.build.model.json.bintray.BintrayPackageInfoJson
import ru.surfstudio.android.build.model.json.bintray.BintrayRepoLatestVersionJson

/**
 * Class to use bintray api
 */
internal class BintrayRepository {

    private val gson = GsonBuilder().create()

    /**
     * Get artifact version
     */
    fun isArtifactVersionExist(artifactName: String, artifactVersion: String): Boolean {
        val response = getResponse("${BintrayConfig.GET_VERSION_URL}/$artifactName/versions/$artifactVersion")

        if (response.statusCode == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED) {
            throw UnauthorizedException(response.toString())
        }

        return response.isSuccessful
    }

    fun getAllPackages(): List<String> {
        val response = getResponse(BintrayConfig.GET_ALL_PACKAGES_URL)

        if (response.isSuccessful) {
            val data = String(response.data)
            return gson.fromJson(data, Array<BintrayPackageInfoJson>::class.java)
                    .toList()
                    .map {
                        it.transform()
                    }
        } else {
            if (response.statusCode == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED) {
                throw UnauthorizedException(response.toString())
            }
            throw UnableGetBintrayPackagesException(response.toString())
        }
    }

    fun getArtifactLatestVersion(artifactName: String): BintrayRepoLatestVersion {
        val response = getResponse("${BintrayConfig.GET_VERSION_URL}/$artifactName/versions/_latest")

        if (response.isSuccessful) {
            val data = String(response.data)
            return gson.fromJson(data, BintrayRepoLatestVersionJson::class.java).transform()
        } else {
            if (response.statusCode == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED) {
                throw UnauthorizedException(response.toString())
            }
            throw UnableGetBintrayPackagesException(response.toString())
        }
    }

    private fun getResponse(url: String) =
            Fuel.get(url)
                    .authentication()
                    .basic(BintrayConfig.USER_NAME, BintrayConfig.PASSWORD)
                    .response()
                    .second
}