package ru.surfstudio.android.build.model.dependency

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Common dependency class
 */
open class Dependency(
        open val name: String = EMPTY_STRING,
        open val type: DependencyType = DependencyType.IMPLEMENTATION
)