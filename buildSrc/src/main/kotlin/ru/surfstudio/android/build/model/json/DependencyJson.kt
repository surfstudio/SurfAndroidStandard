package ru.surfstudio.android.build.model.json

import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about dependencies json object
 */
data class DependencyJson(
        val name: String = EMPTY_STRING,
        val type: String = EMPTY_STRING
)