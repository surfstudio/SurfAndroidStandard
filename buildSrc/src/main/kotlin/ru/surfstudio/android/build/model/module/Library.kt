package ru.surfstudio.android.build.model.module

import ru.surfstudio.android.build.model.dependency.AndroidStandardDependency
import ru.surfstudio.android.build.model.dependency.ThirdPartyDependency
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent information about library
 */
class Library(
        override val name: String = EMPTY_STRING,
        override val directoryPath: String = EMPTY_STRING,
        val directory: String = EMPTY_STRING,
        val artifactName: String = EMPTY_STRING,
        val thirdPartyDependencies: List<ThirdPartyDependency> = listOf(),
        val androidStandardDependencies: List<AndroidStandardDependency> = listOf(),
        var projectVersion: String = EMPTY_STRING,
        val description: String = EMPTY_STRING,
        val url: String = EMPTY_STRING
) : Module(name, directoryPath)