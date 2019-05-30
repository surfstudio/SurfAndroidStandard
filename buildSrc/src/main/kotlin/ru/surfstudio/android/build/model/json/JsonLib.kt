package ru.surfstudio.android.build.model.json

import com.beust.klaxon.Json
import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about libs json object
 */
data class JsonLib(
        val name: String = EMPTY_STRING,
        val dir: String = EMPTY_STRING,
        @Json(name = "artifact_name")
        val artifactName: String = EMPTY_STRING,
        @Json(name = "third_party_dependencies")
        val thirdPartyDependencies: List<JsonDependency> = listOf(),
        @Json(name = "android_standard_dependencies")
        val androidStandardDependencies: List<JsonDependency> = listOf()
)