package ru.surfstudio.android.build.tasks.check_components

import com.beust.klaxon.Klaxon
import ru.surfstudio.android.build.model.CheckComponentsConfigurationResult
import ru.surfstudio.android.build.model.Component
import java.io.File

/**
 * Class for checking if files in components are changed beyween revisions
 *
 * @param currentRevision current revision of project to check
 * @param revisionToCompare revision of this project to compare with current
 */
class ComponentsFilesChecker(val currentRevision: String,
                             val revisionToCompare: String
) {
    private val componentsJsonFilePath = "$currentDirectory/buildSrc/components.json"

    fun checkComponentsFilesChanged(): CheckComponentsConfigurationResult {
        val changedComponents = getChangedComponents()
        return if (changedComponents.isEmpty()) CheckComponentsConfigurationResult(true)
        else CheckComponentsConfigurationResult(false, "Components files changed \n" +
                " ${changedComponents.map { it.name }}")
    }

    private fun getChangedComponents(): List<Component> {
        val diffResults = GitCommandRunner().diff(currentRevision, revisionToCompare)
        return if (diffResults.isNullOrEmpty()) emptyList()
        else {
            val components = parseComponentJson(componentsJsonFilePath)
            components.filter { it.stable }.filter { currentComponent ->
                isComponentChanged(currentComponent, diffResults)
            }
        }
    }

    private fun isComponentChanged(currentComponent: Component, diffResults: List<String>): Boolean {
        return currentComponent.libraries.filter { library ->
            val libraryDir = "${currentComponent.directory}/${library.directory}"
            return diffResults.find { s -> s.contains(libraryDir, ignoreCase = true) } != null
        }.isNotEmpty()
    }

    private fun parseComponentJson(path: String): List<Component> {
        return Klaxon().parseArray(File(path))
                ?: throw RuntimeException("Can't parse components.json")
    }
}