package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_INT
import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about project
 */
data class ProjectInformation(
        val name: String = EMPTY_STRING,
        val version: Int = EMPTY_INT
) {

    val isEmpty get() = name.isEmpty()
}