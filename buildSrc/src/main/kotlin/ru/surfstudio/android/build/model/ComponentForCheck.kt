package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.model.json.ComponentJson

/**
 * Represent information about component with library versions
 */
class ComponentForCheck(
        val id: String,
        val directory: String,
        val version: String,
        val isStable: Boolean,
        val libs: List<LibraryForCheck> = listOf()
) {
    companion object {

        fun create(component: ComponentJson)
                : ComponentForCheck {
            return ComponentForCheck(
                    component.id,
                    component.dir,
                    component.version,
                    component.stable,
                    component.libs.map { LibraryForCheck.create(it) }
            )
        }
    }
}