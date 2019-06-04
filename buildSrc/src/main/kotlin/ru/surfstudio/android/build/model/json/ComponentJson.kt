package ru.surfstudio.android.build.model.json

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.EMPTY_INT
import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about component json object
 */
data class ComponentJson(
        val id: String = EMPTY_STRING,
        val version: String = EMPTY_STRING,
        val stable: Boolean = false,
        @SerializedName("unstable_version") val unstableVersion: Int = EMPTY_INT,
        val dir: String = EMPTY_STRING,
        val libs: List<LibJson> = listOf(),
        val samples: List<SampleJson> = listOf(),
        @SerializedName("has_mirror") val hasMirror: Boolean = false,
        @SerializedName("mirror_repo") val mirrorRepo: String = EMPTY_STRING
)