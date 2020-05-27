package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.changed_components.models.*

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
    fun compareProjectConfigurations(): List<ComponentCheckResult> {
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
            if (pair.second == null) {
                ComponentCheckResult.create(pair.first, true, ComponentChangeReason.COMPONENT_REMOVED)
            } else {
                compareComponentsPair(pair.first, pair.second!!)
            }
        }
    }

    /**
     * compares each component with other by comparing libraries lists
     *
     * @param firstComponent first component
     * @param secondComponent second component
     *
     * @return corresponding component check result depending on result of check
     */
    private fun compareComponentsPair(firstComponent: ComponentWithVersion, secondComponent: ComponentWithVersion): ComponentCheckResult {
        return if (!areLibrariesListsEqual(firstComponent.libs, secondComponent.libs)) {
            ComponentCheckResult.create(firstComponent, true, ComponentChangeReason.LIBRARIES_DIFFER)
        } else {
            compareLibraries(firstComponent, firstComponent.libs, secondComponent.libs)
        }
    }

    /**
     * checks every library for change. Returns as soon as any library differ
     *
     * @param component component for which comparing is made
     * @param firstLibraryList first libraries list
     * @param secondLibraryList second libraries list
     *
     * @return corresponding component check result depending on result of check
     */
    private fun compareLibraries(
            component: ComponentWithVersion,
            firstLibraryList: List<LibraryWithVersion>,
            secondLibraryList: List<LibraryWithVersion>
    ): ComponentCheckResult {

        firstLibraryList.forEach { libraryFirst ->
            val librarySecond = secondLibraryList.find { it.name == libraryFirst.name }!!
            val result = compareDependencies(component, libraryFirst, librarySecond)
            if (result.isComponentChanged) {
                return result
            }
        }

        return ComponentCheckResult.create(component, false)
    }

    /**
     * checks if libraries list contain the same elements
     *
     * @param firstLibraryList first libraries list
     * @param secondLibraryList second libraries list
     *
     * @return true if firstComponent list contain secondComponent and vice versa
     */
    private fun areLibrariesListsEqual(firstLibraryList: List<LibraryWithVersion>, secondLibraryList: List<LibraryWithVersion>) =
            firstLibraryList.containsAll(secondLibraryList) && secondLibraryList.containsAll(firstLibraryList)

    /**
     * compares libraries` dependencies: thirdPartyDependencies and androidStandardDependencies
     *
     * @param component component for which comparing is made
     * @param libraryFirst first library to compare
     * @param librarySecond second library to compare
     *
     * @return corresponding component check result depending on result of check
     */
    private fun compareDependencies(
            component: ComponentWithVersion,
            libraryFirst: LibraryWithVersion,
            librarySecond: LibraryWithVersion
    ): ComponentCheckResult {

        val thirdPartyDependenciesEqualResult = areThirdPartyDependenciesEqual(libraryFirst, librarySecond)
        val androidStandardDependenciesResult = areAndroidStandardDependenciesEqual(libraryFirst, librarySecond)

        if (!thirdPartyDependenciesEqualResult) {
            return ComponentCheckResult.create(component, true, ComponentChangeReason.THIRD_PARTY_DEPENDENCIES_DIFFER)
        }
        if (!androidStandardDependenciesResult) {
            return ComponentCheckResult.create(component, true, ComponentChangeReason.ANDROID_STANDARD_DEPENDENCIES_DIFFER)
        }

        return ComponentCheckResult.create(component, false)
    }

    /**
     * checks if third Party Dependencies of libraries are equal
     *
     * @param libraryFirst first library
     * @param librarySecond second library
     *
     * @return true if lists are equal
     */
    private fun areThirdPartyDependenciesEqual(libraryFirst: LibraryWithVersion, librarySecond: LibraryWithVersion) =
            libraryFirst.thirdPartyDependencies.isEqualToList(librarySecond.thirdPartyDependencies)

    /**
     * checks if android standard Dependencies of libraries are equal
     *
     * @param libraryFirst first library
     * @param librarySecond second library
     *
     * @return true if lists are equal
     */
    private fun areAndroidStandardDependenciesEqual(libraryFirst: LibraryWithVersion, librarySecond: LibraryWithVersion) =
            libraryFirst.androidStandardDependencies.isEqualToList(librarySecond.androidStandardDependencies)

    /**
     * helper method for comparing lists
     *
     * @return true if lists contain same elements
     */
    private fun <T> List<T>.isEqualToList(anotherList: List<T>?): Boolean {
        return hashSetOf(this) == hashSetOf(anotherList)
    }
}