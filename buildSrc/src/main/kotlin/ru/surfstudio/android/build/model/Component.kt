package ru.surfstudio.android.build.model

import com.beust.klaxon.Json

/**
 * Проедставляет информацию о компоненте
 */
data class Component(
        val id: String = "",
        val version: String = "",
        val stable: Boolean = false,
        val dir: String = "",
        val libs: List<Lib> = listOf(),
        val samples: List<Sample> = listOf(),
        @Json(name = "has_mirror") val hasMirror: Boolean = false,
        @Json(name = "mirror_repo") val mirrorRepo: String = ""
)