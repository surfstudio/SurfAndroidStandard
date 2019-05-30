package ru.surfstudio.android.build.model.json

import ru.surfstudio.android.build.EMPTY_INT
import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about project snapshot json object
 */
data class JsonProjectSnapshot(
        val name: String = EMPTY_STRING,
        val version: Int = EMPTY_INT
) {

    val isEmpty get() = name.isEmpty()
}