package ru.surfstudio.android.build.model.dependency

import ru.surfstudio.android.build.EMPTY_STRING
import ru.surfstudio.android.build.model.json.DependencyJson

/**
 * Represent information about third party dependency
 */
data class ThirdPartyDependency(
        override val name: String = EMPTY_STRING,
        override val type: String = EMPTY_STRING
) : Dependency(name, type) {

    companion object {

        fun create(dependencyJson: DependencyJson) = ThirdPartyDependency(
                name = dependencyJson.name,
                type = dependencyJson.type
        )
    }
}