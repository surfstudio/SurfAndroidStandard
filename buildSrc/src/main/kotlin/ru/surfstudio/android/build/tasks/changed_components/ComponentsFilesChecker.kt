package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.model.CheckComponentChangedResult
import ru.surfstudio.android.build.model.ComponentChangeReason
import ru.surfstudio.android.build.model.ComponentForCheck
import ru.surfstudio.android.build.tasks.currentDirectory

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
    private val currentComponentsJsonPath = "$currentDirectory/buildSrc/components.json"

    /**
     * Get info for every component, whether its files have changed between [currentRevision] and [revisionToCompare]
     *
     * @return information about changes for every component
     */
    fun getChangeInformationForComponents(): List<CheckComponentChangedResult> {
        val diffResults = getDiffBetweenRevisions()
        val currentComponents = JsonHelper.parseComponentJson(currentComponentsJsonPath).map { ComponentForCheck.create(it) }

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
    private fun generateAllComponentsNotChangedResults(currentComponents: List<ComponentForCheck>): List<CheckComponentChangedResult> {
        return currentComponents.map { CheckComponentChangedResult.create(it, false) }
    }

    /**
     * for every component checks information for it from git diff command and returns result
     *
     * @param diffResults results for git diff command
     * @param currentComponents current components to check and get results
     *
     * @return information about changes for every component
     */
    private fun getCheckComponentsResultsWithDiff(diffResults: List<String>, currentComponents: List<ComponentForCheck>)
            : List<CheckComponentChangedResult> {
        return currentComponents.map { component ->
            if (isComponentChanged(component, diffResults))
                CheckComponentChangedResult.create(component, true, ComponentChangeReason.FILE_CHANGED)
            else
                CheckComponentChangedResult.create(component, false)

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
    private fun isComponentChanged(currentComponent: ComponentForCheck, diffResults: List<String>): Boolean {
        return currentComponent.libs
                .filter { library ->
                    val libraryDir = "${currentComponent.directory}/${library.directory}"
                    return diffResults.find { s -> s.contains(libraryDir, ignoreCase = true) } != null
                }.isNotEmpty()
    }
}