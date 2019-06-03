package ru.surfstudio.android.build.model.json

import com.beust.klaxon.Json
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent information about libs json object
 */
data class LibJson(
        val name: String = EMPTY_STRING,
        val dir: String = EMPTY_STRING,
        @Json(name = "artifact_name")
        val artifactName: String = EMPTY_STRING,
        @Json(name = "third_party_dependencies")
        val thirdPartyDependencies: List<DependencyJson> = listOf(),
        @Json(name = "android_standard_dependencies")
        val androidStandardDependencies: List<DependencyJson> = listOf()
)