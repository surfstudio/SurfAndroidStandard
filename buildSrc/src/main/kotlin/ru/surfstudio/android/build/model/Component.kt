package ru.surfstudio.android.build.model

import com.beust.klaxon.Json
import ru.surfstudio.android.build.EMPTY_INT
import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about component
 */
data class Component(
        val id: String = EMPTY_STRING,
        val version: String = EMPTY_STRING,
        val stable: Boolean = false,
        @Json(name = "unstable_version") val unstableVersion: Int = EMPTY_INT,
        val dir: String = EMPTY_STRING,
        val libs: List<Lib> = listOf(),
        val samples: List<Sample> = listOf(),
        @Json(name = "has_mirror") val hasMirror: Boolean = false,
        @Json(name = "mirror_repo") val mirrorRepo: String = EMPTY_STRING
)