package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about library
 */
class Library(
        override val name: String = EMPTY_STRING,
        override val directory: String = EMPTY_STRING,
        val artifactName: String = EMPTY_STRING,
        val thirdPartyDependencies: List<Dependency> = listOf(),
        val androidStandardDependencies: List<Dependency> = listOf()
) : Module(name, directory)