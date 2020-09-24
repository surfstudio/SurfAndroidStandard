package ru.surfstudio.android.build.model.dependency

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent information about third party dependency
 */
data class ThirdPartyDependency(
        override val name: String = EMPTY_STRING,
        override val type: String = EMPTY_STRING
) : Dependency(name, type) {

    val artifactGroupId = name.substringBefore(':')
    val artifactName = name.substringAfter(':')
}