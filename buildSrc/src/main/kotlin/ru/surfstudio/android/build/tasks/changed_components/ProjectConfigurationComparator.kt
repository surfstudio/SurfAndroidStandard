package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.changed_components.models.ComponentChangeReason
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentWithVersion
import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration

/**
 * Helper class for comparing two ProjectConfigurationInfo objects, representing information about project configuration state
 * and getting information, which components in first configuration have changed with reason of change
 *
 * @param firstProjectConfiguration configuration which components configurations are taken from and comparing to [secondProjectConfiguration]
 * @param secondProjectConfiguration configuration which [firstProjectConfiguration] is compared with
 */
class ProjectConfigurationComparator(
        private val firstProjectConfiguration: ProjectConfiguration,
        private val secondProjectConfiguration: ProjectConfiguration
) {

    /**
     * compares two project configuration
     *
     * @return for every component from [firstProjectConfiguration] returns information whether it was changed or not
     * and if changed why
     */
    fun compareProjectInfos(): List<ComponentCheckResult> {
        return if (areGeneralValuesDifferent(firstProjectConfiguration, secondProjectConfiguration)) {
            generateAllComponentsChanged()
        } else {
            compareAllComponents(firstProjectConfiguration.components, secondProjectConfiguration.components)
        }
    }

    /**
     * generates information for every component from first configuration that it was changed due to general configuration changes
     */
    private fun generateAllComponentsChanged(): List<ComponentCheckResult> {
        return firstProjectConfiguration.components.map { componentWithVersion ->
            ComponentCheckResult.create(componentWithVersion, true, ComponentChangeReason.GENERAL_VALUES_DIFFER)
        }
    }

    /**
     * compares values which refer to all components: val libMinSdkVersion, targetSdkVersion, moduleVersionCode, compileSdkVersion
     *
     * @return true if differ else false
     */
    private fun areGeneralValuesDifferent(
            firstProjectConfiguration: ProjectConfiguration,
            secondProjectConfiguration: ProjectConfiguration
    ): Boolean {
        return !firstProjectConfiguration.equals(secondProjectConfiguration)
    }

    /**
     * for every component from [firstProjectConfiguration] compares it with corresponding component from [secondProjectConfiguration]
     *
     * @param first list of components from [firstProjectConfiguration]
     * @param second list of components from [secondProjectConfiguration]
     *
     * @return list of ComponentCheckResult with result of check for every component from [firstProjectConfiguration]
     */
    private fun compareAllComponents(
            first: List<ComponentWithVersion>,
            second: List<ComponentWithVersion>
    ): List<ComponentCheckResult> {

        val pairs = first.map { componentWithVersion ->
            componentWithVersion to second.find { it.id == componentWithVersion.id }
        }
        return pairs.map { pair ->
            if (pair.second == null)
                ComponentCheckResult.create(pair.first, true, ComponentChangeReason.COMPONENT_REMOVED)
            else
                compareComponents(pair.first, pair.second!!)
        }
    }

    /**
     * compare each component`s libraries
     *
     * @param first first component
     * @param second second component
     *
     * @return corresponding component check result depending on result of check
     */
    private fun compareComponents(first: ComponentWithVersion, second: ComponentWithVersion): ComponentCheckResult {
        return if (!first.libs.isEqualToList(second.libs))
            ComponentCheckResult.create(first, true, ComponentChangeReason.LIBRARIES_DIFFER)
        else
            ComponentCheckResult.create(first, false)
    }

    /**
     * helper method for comparing lists
     *
     * @return true if lists contain same elements
     */
    private fun <T> List<T>.isEqualToList(anotherList: List<T>): Boolean {
        return hashSetOf(this) == hashSetOf(anotherList)
    }
}