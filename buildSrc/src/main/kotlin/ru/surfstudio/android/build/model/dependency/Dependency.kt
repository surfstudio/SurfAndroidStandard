package ru.surfstudio.android.build.model.dependency

import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Common dependency class
 */
open class Dependency(
        open val name: String = EMPTY_STRING,
        open val type: String = EMPTY_STRING
)