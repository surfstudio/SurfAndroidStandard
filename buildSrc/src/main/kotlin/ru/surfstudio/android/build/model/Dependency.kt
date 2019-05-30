package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about dependencies
 */
data class Dependency(
        val name: String = EMPTY_STRING,
        val type: String = EMPTY_STRING
)