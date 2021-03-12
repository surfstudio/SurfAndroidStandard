package ru.surfstudio.android.build.model.dependency

import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent information about android standard dependency
 */
data class AndroidStandardDependency(
        override val name: String = EMPTY_STRING,
        override val type: DependencyType = DependencyType.IMPLEMENTATION,
        var component: Component = Component()
) : Dependency(name, type)