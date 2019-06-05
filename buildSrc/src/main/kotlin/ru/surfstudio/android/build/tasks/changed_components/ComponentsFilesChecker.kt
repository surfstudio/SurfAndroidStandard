package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentChangeReason
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentWithVersion

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
    fun getChangeInformationForComponents(): List<ComponentCheckResult> {
        val diffResults = getDiffBetweenRevisions()
        val currentComponents = Components.value
                .map { ComponentWithVersion.create(it) }

        return if (diffResults.isNullOrEmpty()) generateAllComponentsNotChangedResults(currentComponents)
        else {
            getCheckComponentsResultsWithDiff(diffResults, currentComponents)
        }
    }

    /**
     * for every component generates result meaning no changes has happened to component
     *
     * @param currentComponents current components to check and get results
     *
     * @return information every component has not changed
     */
    private fun generateAllComponentsNotChangedResults(currentComponents: List<ComponentWithVersion>): List<ComponentCheckResult> {
        return currentComponents.map { ComponentCheckResult.create(it, false) }
    }

    /**
     * for every component checks information for it from git diff command and returns result
     *
     * @param diffResults results for git diff command
     * @param currentComponents current components to check and get results
     *
     * @return information about changes for every component
     */
    private fun getCheckComponentsResultsWithDiff(diffResults: List<String>, currentComponents: List<ComponentWithVersion>)
            : List<ComponentCheckResult> {
        return currentComponents.map { component ->
            if (isComponentChanged(component, diffResults))
                ComponentCheckResult.create(component, true, ComponentChangeReason.FILE_CHANGED)
            else
                ComponentCheckResult.create(component, false)

        }
    }

    /**
     * return results for git diff command for [currentRevision] and [revisionToCompare]
     *
     * @return output from git diff command
     */
    private fun getDiffBetweenRevisions(): List<String>? {
        return GitCommandRunner().diff(currentRevision, revisionToCompare)
    }

    /**
     * defines if component files (including sources and buil.gradle files) have changed
     *
     * @param currentComponent component for check
     * @param diffResults results for git diff command
     *
     * @return true if changed
     */
    private fun isComponentChanged(currentComponent: ComponentWithVersion, diffResults: List<String>): Boolean {
        return currentComponent.libs.any { library ->
            diffResults.find { s -> s.contains(library.directory, ignoreCase = true) } != null
        }
    }
}