package ru.surfstudio.android.build.model.json

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about libs json object
 */
data class LibJson(
        val name: String = EMPTY_STRING,
        val dir: String = EMPTY_STRING,
        @SerializedName("artifact_name")
        val artifactName: String = EMPTY_STRING,
        @SerializedName("third_party_dependencies")
        val thirdPartyDependencies: List<DependencyJson> = listOf(),
        @SerializedName("android_standard_dependencies")
        val androidStandardDependencies: List<DependencyJson> = listOf()
)