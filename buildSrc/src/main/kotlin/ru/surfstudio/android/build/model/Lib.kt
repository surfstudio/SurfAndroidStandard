package ru.surfstudio.android.build.model

import com.beust.klaxon.Json
import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about libs
 */
data class Lib(
        val name: String = EMPTY_STRING,
        val dir: String = EMPTY_STRING,
        @Json(name = "artifact_name")
        val artifactName: String = EMPTY_STRING,
        @Json(name = "third_party_dependencies")
        val thirdPartyDependencies: List<Dependency> = listOf(),
        @Json(name = "android_standard_dependencies")
        val androidStandardDependencies: List<Dependency> = listOf()
)