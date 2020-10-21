package ru.surfstudio.android.build.tasks.changed_components.models

import ru.surfstudio.android.build.model.Component

/**
 * Represent information about components check result
 *
 * [componentName] name of component the result is returned for
 * [isComponentChanged] if component has changed
 * [isComponentStable] if component is stable
 * [reasonOfComponentChange] if component changed reason of changes
 */
class ComponentCheckResult private constructor(
        val componentName: String,
        val isComponentStable: Boolean,
        val isComponentChanged: Boolean,
        val reasonOfComponentChange: ComponentChangeReason
) {

    companion object {

        fun create(
                component: Component,
                isChanged: Boolean,
                reasonOfComponentChange: ComponentChangeReason = ComponentChangeReason.NO_REASON
        ) = ComponentCheckResult(component.name, component.stable, isChanged, reasonOfComponentChange)


        fun create(
                componentWithVersion: ComponentWithVersion,
                isChanged: Boolean,
                reasonOfComponentChange: ComponentChangeReason = ComponentChangeReason.NO_REASON
        ) = ComponentCheckResult(componentWithVersion.id, componentWithVersion.isStable, isChanged, reasonOfComponentChange)
    }
}