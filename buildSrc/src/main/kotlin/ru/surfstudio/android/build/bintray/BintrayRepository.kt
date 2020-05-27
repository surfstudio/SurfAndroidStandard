package ru.surfstudio.android.build.bintray

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.isSuccessful
import com.google.gson.GsonBuilder
import org.gradle.api.GradleException
import org.gradle.internal.impldep.com.google.api.client.http.HttpStatusCodes
import ru.surfstudio.android.build.exceptions.UnauthorizedException
import ru.surfstudio.android.build.model.BintrayRepoLatestVersion
import ru.surfstudio.android.build.model.json.bintray.BintrayPackageInfoJson
import ru.surfstudio.android.build.model.json.bintray.BintrayRepoLatestVersionJson

/**
 * Any Bintray response can suddenly return -1 error code with empty response.
 * These requests need to be repeated
 */
private const val EMPTY_ERROR_CODE = -1

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

    /**
     * Function for getting all packages from bintray
     */
    fun getAllPackages(needRetry: Boolean = true): List<String> {
        val response = getResponse(BintrayConfig.GET_ALL_PACKAGES_URL)

        if (response.isSuccessful) {
            val data = String(response.data)
            return gson.fromJson(data, Array<BintrayPackageInfoJson>::class.java)
                    .toList()
                    .map {
                        it.transform()
                    }
        } else {
            val stringResponse = response.toString()
            when (response.statusCode) {
                HttpStatusCodes.STATUS_CODE_UNAUTHORIZED -> throw UnauthorizedException(stringResponse)
                EMPTY_ERROR_CODE -> {
                    if (needRetry) {
                        println("$stringResponse\n RETRY ${BintrayConfig.GET_ALL_PACKAGES_URL}")
                        return getAllPackages(false)
                    } else {
                        throw GradleException(stringResponse)
                    }
                }
                else -> throw GradleException(stringResponse)
            }
        }
    }

    /**
     * Function for getting the latest version of artifact in bintray
     */
    fun getArtifactLatestVersion(
            artifactName: String,
            needRetry: Boolean = true
    ): BintrayRepoLatestVersion {
        val url = "${BintrayConfig.GET_VERSION_URL}/$artifactName/versions/_latest"
        val response = getResponse(url)

        if (response.isSuccessful) {
            val data = String(response.data)
            return gson.fromJson(data, BintrayRepoLatestVersionJson::class.java).transform()
        } else {
            val stringResponse = response.toString()
            when (response.statusCode) {
                HttpStatusCodes.STATUS_CODE_UNAUTHORIZED -> throw UnauthorizedException(stringResponse)
                EMPTY_ERROR_CODE -> {
                    if (needRetry) {
                        println("$stringResponse\n RETRY $url")
                        return getArtifactLatestVersion(artifactName, false)
                    } else {
                        throw GradleException(stringResponse)
                    }
                }
                else -> throw GradleException(stringResponse)
            }
        }
    }

    private fun getResponse(url: String) =
            Fuel.get(url)
                    .authentication()
                    .basic(BintrayConfig.USER_NAME, BintrayConfig.PASSWORD)
                    .response()
                    .second
}