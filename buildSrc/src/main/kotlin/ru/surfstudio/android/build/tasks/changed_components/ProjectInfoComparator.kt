package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.model.CheckComponentChangedResult
import ru.surfstudio.android.build.model.ComponentChangeReason
import ru.surfstudio.android.build.model.ComponentForCheck
import ru.surfstudio.android.build.model.ProjectConfiguration

/**
 * Helper class for comparing two ProjectConfigurationInfo objects, representing information about project configuration state
 * and getting information, which components in first configuration have changed with reason of change
 *
 * @param firstProjectConfiguration configuration which components configurations are taken from and comparing to [secondProjectConfiguration]
 * @param secondProjectConfiguration configuration which [firstProjectConfiguration] is compared with
 */
class ProjectInfoComparator(
        private val firstProjectConfiguration: ProjectConfiguration,
        private val secondProjectConfiguration: ProjectConfiguration
) {

    /**
     * compares two project configuration
     *
     * @return for every component from [firstProjectConfiguration] returns information whether it was changed or not
     * and if changed why
     */
    fun compareProjectInfos(): List<CheckComponentChangedResult> {
        return if (compareGeneralValues(firstProjectConfiguration, secondProjectConfiguration)) {
            generateAllComponentsChanged()
        } else {
            compareAllComponents(firstProjectConfiguration.components, secondProjectConfiguration.components)
        }
    }

    /**
     * generates information for every component from first configuration that it was changed due to general configuration changes
     */
    private fun generateAllComponentsChanged(): List<CheckComponentChangedResult> {
        return firstProjectConfiguration.components.map { componentWithVersion ->
            CheckComponentChangedResult.create(componentWithVersion, true, ComponentChangeReason.GENERAL_VALUES_DIFFER)
        }
    }

    private fun compareGeneralValues(
            firstProjectConfiguration: ProjectConfiguration,
            secondProjectConfiguration: ProjectConfiguration
    ): Boolean {
        return firstProjectConfiguration.equals(secondProjectConfiguration)
    }

    private fun compareAllComponents(
            first: List<ComponentForCheck>,
            second: List<ComponentForCheck>
    ): List<CheckComponentChangedResult> {

        val pairs = first.map { componentWithVersion ->
            componentWithVersion to second.find { it.id == componentWithVersion.id }
        }
        return pairs.map { pair ->
            if (pair.second == null)
                CheckComponentChangedResult.create(pair.first, true, ComponentChangeReason.COMPONENT_REMOVED)
            else
                compareComponents(pair.first, pair.second!!)
        }
    }

    private fun compareComponents(first: ComponentForCheck, second: ComponentForCheck): CheckComponentChangedResult {
        return if (first.libs.isEqualToList(second.libs))
            CheckComponentChangedResult.create(first, true, ComponentChangeReason.LIBRARIES_DIFFER)
        else
            CheckComponentChangedResult.create(first, false)
    }

    private fun <T> List<T>.isEqualToList(anotherList: List<T>): Boolean {
        return hashSetOf(this) == hashSetOf(anotherList)
    }
}