package ru.surfstudio.android.build.bintray

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.isSuccessful
import org.gradle.internal.impldep.com.google.api.client.http.HttpStatusCodes
import ru.surfstudio.android.build.exceptions.UnauthorizedException

/**
 * Class to use bintray api
 */
internal class BintrayRepository {

    /**
     * Get artifact version
     */
    fun isArtifactVersionExist(artifactName: String, artifactVersion: String): Boolean {
        val response = Fuel.get("${BintrayConfig.GET_VERSION_URL}/$artifactName/versions/$artifactVersion")
                .authentication()
                .basic(BintrayConfig.USER_NAME, BintrayConfig.PASSWORD)
                .response()
                .second

        if (response.statusCode == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED) {
            throw   UnauthorizedException(response.toString())
        }

        return response.isSuccessful
    }
}