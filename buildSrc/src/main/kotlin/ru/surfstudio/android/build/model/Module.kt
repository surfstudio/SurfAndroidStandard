package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Parent class for project's module
 */
open class Module(
        open val name: String = EMPTY_STRING,
        open val directory: String = EMPTY_STRING
) {

    /**
     * Configuration name e.x. ":test"
     */
    val gradleModuleName get() = ":$name"
}