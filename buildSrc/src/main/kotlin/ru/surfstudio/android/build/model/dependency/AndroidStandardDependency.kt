package ru.surfstudio.android.build.model.dependency

import ru.surfstudio.android.build.EMPTY_STRING
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.json.DependencyJson

/**
 * Represent information about android standard dependency
 */
data class AndroidStandardDependency(
        override val name: String = EMPTY_STRING,
        override val type: String = EMPTY_STRING,
        var component: Component = Component()
) : Dependency(name, type) {

    companion object {

        fun create(dependencyJson: DependencyJson) = AndroidStandardDependency(
                name = dependencyJson.name,
                type = dependencyJson.type
        )
    }
}