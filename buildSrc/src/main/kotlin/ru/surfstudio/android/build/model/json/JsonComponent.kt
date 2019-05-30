package ru.surfstudio.android.build.model.json

import com.beust.klaxon.Json
import ru.surfstudio.android.build.EMPTY_INT
import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about component json object
 */
data class JsonComponent(
        val id: String = EMPTY_STRING,
        val version: String = EMPTY_STRING,
        val stable: Boolean = false,
        @Json(name = "unstable_version") val unstableVersion: Int = EMPTY_INT,
        val dir: String = EMPTY_STRING,
        val libs: List<JsonLib> = listOf(),
        val samples: List<JsonSample> = listOf(),
        @Json(name = "has_mirror") val hasMirror: Boolean = false,
        @Json(name = "mirror_repo") val mirrorRepo: String = EMPTY_STRING
)