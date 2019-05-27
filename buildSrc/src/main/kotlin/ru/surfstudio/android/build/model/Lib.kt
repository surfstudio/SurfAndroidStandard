package ru.surfstudio.android.build.model

import com.beust.klaxon.Json

/**
 * Представляет информацию о библиотеках, которые мужны компоненту
 */
data class Lib(
        val name: String = "",
        val dir: String = "",
        @Json(name = "artifact_name")
        val artifactName: String = "",
        @Json(name = "third_party_dependencies")
        val thirdPartyDependencies: List<Dependency> = listOf(),
        @Json(name = "android_standard_dependencies")
        val androidStandardDependencies: List<Dependency> = listOf()
)