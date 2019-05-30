package ru.surfstudio.android.build.tasks.check_components

import ru.surfstudio.android.build.Initializator.Companion.components
import ru.surfstudio.android.build.model.CheckComponentsConfigurationResult
import ru.surfstudio.android.build.model.Component

/**
 * Class for checking if files in components are changed beyween revisions
 *
 * @param currentRevision current revision of project to check
 * @param revisionToCompare revision of this project to compare with current
 */
class ComponentsFilesChecker(val currentRevision: String,
                             val revisionToCompare: String
) {

    fun checkComponentsFilesChanged(): CheckComponentsConfigurationResult {
        val changedComponents = getChangedComponents()
        return if (changedComponents.isEmpty()) CheckComponentsConfigurationResult(true)
        else CheckComponentsConfigurationResult(false, "Components files changed \n" +
                " ${changedComponents.map { it.id }}")
    }

    private fun getChangedComponents(): List<Component> {
        val diffResults = GitCommandRunner().diff(currentRevision, revisionToCompare)
        return if (diffResults.isNullOrEmpty()) emptyList()
        else components.filter { it.stable }.filter { currentComponent ->
            isComponentChanged(currentComponent, diffResults)
        }
    }

    private fun isComponentChanged(currentComponent: Component, diffResults: List<String>): Boolean {
        return currentComponent.libs.filter { library ->
            val libraryDir = "${currentComponent.dir}/${library.dir}"
            return diffResults.find { s -> s.contains(libraryDir, ignoreCase = true) } != null
        }.isNotEmpty()
    }
}