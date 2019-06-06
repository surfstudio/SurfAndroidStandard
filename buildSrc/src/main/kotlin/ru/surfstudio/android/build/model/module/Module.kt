package ru.surfstudio.android.build.model.module

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Parent class for project's module
 */
open class Module(
        open val name: String = EMPTY_STRING,
        open val directoryPath: String = EMPTY_STRING
) {

    /**
     * Configuration name e.x. ":test"
     */
    val gradleModuleName get() = ":$name"
}