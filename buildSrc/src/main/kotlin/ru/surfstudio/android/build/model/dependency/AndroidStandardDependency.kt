package ru.surfstudio.android.build.model.dependency

import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.model.Component

/**
 * Represent information about android standard dependency
 */
data class AndroidStandardDependency(
        override val name: String = EMPTY_STRING,
        override val type: String = EMPTY_STRING,
        var component: Component = Component()
) : Dependency(name, type)