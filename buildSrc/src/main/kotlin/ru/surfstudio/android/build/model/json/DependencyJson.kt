package ru.surfstudio.android.build.model.json

import ru.surfstudio.android.build.model.dependency.AndroidStandardDependency
import ru.surfstudio.android.build.model.dependency.Dependency
import ru.surfstudio.android.build.model.dependency.DependencyType
import ru.surfstudio.android.build.model.dependency.ThirdPartyDependency
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent information about dependencies json object
 */
data class DependencyJson(
        val name: String = EMPTY_STRING,
        val type: String = EMPTY_STRING
) {

    constructor(dependency: Dependency) : this(
            name = dependency.name,
            type = dependency.type.gradleType
    )

    fun transformToAndroidStandardDependency() = AndroidStandardDependency(
            name, DependencyType.fomString(type)
    )

    fun transformToThirdPartyDependency() = ThirdPartyDependency(
            name, DependencyType.fomString(type)
    )
}