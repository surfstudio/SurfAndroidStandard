package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentChangeReason
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult

/**
 * Class for checking if files in components are changed between revisions
 *
 * @param currentRevision current revision of project to check
 * @param revisionToCompare revision of this project to compare with current
 */
class ComponentsFilesChecker(
        private val currentRevision: String,
        private val revisionToCompare: String
) {

    /**
     * Get info for every component, whether its files have changed between [currentRevision] and [revisionToCompare]
     *
     * @return information about changes for every component
     */
    fun getChangeInformationForComponents(ignoreReleaseNotesChanges: Boolean = false): List<ComponentCheckResult> {
        val currentComponents = Components.value
        val componentsWithDiff = ComponentsDiffProvider(currentRevision, revisionToCompare, currentComponents).provideComponentsWithDiff(ignoreReleaseNotesChanges)
        return if (componentsWithDiff.isNullOrEmpty()) {
            generateAllComponentsNotChangedResults(currentComponents)
        } else {
            getCheckComponentsResultsWithDiff(componentsWithDiff, currentComponents)
        }
    }

    /**
     * for every component generates result meaning no changes has happened to component
     *
     * @param currentComponents current components to check and get results
     *
     * @return information every component has not changed
     */
    private fun generateAllComponentsNotChangedResults(currentComponents: List<Component>): List<ComponentCheckResult> {
        return currentComponents.map { ComponentCheckResult.create(it, false) }
    }

    /**
     * for every component checks information exists from git diff command (including sources and build.gradle files)
     * and returns result
     *
     * @param componentsWithDiff results for git diff command
     *
     * @return information about changes for every component
     */
    private fun getCheckComponentsResultsWithDiff(componentsWithDiff: Map<Component, List<String>>, currentComponents: List<Component>)
            : List<ComponentCheckResult> {
        return currentComponents.map { component ->
            if (componentsWithDiff.containsKey(component)) {
                ComponentCheckResult.create(component, true, ComponentChangeReason.FILE_CHANGED)
            } else {
                ComponentCheckResult.create(component, false)
            }

        }
    }
}