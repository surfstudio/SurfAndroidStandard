package ru.surfstudio.android.build.model

/**
 * Represent information about components check result
 */
data class CheckComponentChangedResult private constructor(
        val componentName: String,
        val isComponentStable: Boolean,
        val isComponentChanged: Boolean,
        val reasonOfComponentChange: ComponentChangeReason) {

    companion object {

        fun create(component: Component, isChanged: Boolean, reasonOfComponentChange: ComponentChangeReason = ComponentChangeReason.NO_REASON)
                : CheckComponentChangedResult {
            return CheckComponentChangedResult(component.name, component.stable, isChanged, reasonOfComponentChange)
        }

        fun create(componentForCheck: ComponentForCheck, isChanged: Boolean, reasonOfComponentChange: ComponentChangeReason = ComponentChangeReason.NO_REASON)
                : CheckComponentChangedResult {
            return CheckComponentChangedResult(componentForCheck.id, componentForCheck.isStable, isChanged, reasonOfComponentChange)
        }
    }
}