package ru.surfstudio.android.build.tasks.changed_components.models

import ru.surfstudio.android.build.model.Component

/**
 * Represent information about component with library versions
 */
class ComponentWithVersion(
        val id: String,
        val directory: String,
        val version: String,
        val isStable: Boolean,
        val libs: List<LibraryWithVersion> = listOf()
) {

    companion object {

        fun create(component: Component) = ComponentWithVersion(
                component.name,
                component.directory,
                component.baseVersion,
                component.stable,
                component.libraries.map { LibraryWithVersion.create(it) }
        )
    }
}