package ru.surfstudio.android.build.model.json.bintray

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.utils.Transformable

/**
 * Response model of package info from bintray
 */
@Deprecated("Use Artifactory or Maven Central after Bintray sunset")
data class BintrayPackageInfoJson(
    @SerializedName("name") val name: String
) : Transformable<String> {

    override fun transform() = name
}