package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.tasks.changed_components.*
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult
import java.io.File

open class IncrementUnstableChangedComponentsTask : DefaultTask() {

    private lateinit var revisionToCompare: String

    @TaskAction
    fun check() {
        println("$INCREMENT_UNSTABLE_CHANGED_TASK_NAME started")

        extractInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        val resultByFiles = ComponentsFilesChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()

        val resultsByConfiguration = ComponentsConfigChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()

        incrementUnstableChanged(resultByFiles, resultsByConfiguration)

        println("$INCREMENT_UNSTABLE_CHANGED_TASK_NAME ended")
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(REVISION_TO_COMPARE)) throw GradleException("please specify $REVISION_TO_COMPARE param")
        revisionToCompare = project.findProperty(REVISION_TO_COMPARE) as String
    }

    private fun incrementUnstableChanged(resultByFiles: List<ComponentCheckResult>, resultByConfigurations: List<ComponentCheckResult>) {
        val currentComponents = Components.value

        val newComponents = currentComponents
                .map { component ->
                    val resultByFile = resultByFiles.find { it.componentName == component.name }
                    val resultByConfig = resultByConfigurations.find { it.componentName == component.name }

                    if (resultByConfig == null || resultByFile == null)
                        throw GradleException("one of the results doesn`t contain information about component ${component.name}")

                    if (isComponentUstableAndChanged(component, resultByFile, resultByConfig)) {
                        println("$INCREMENT_UNSTABLE_CHANGED_TASK_NAME  ${component.name} unstable version inc ")
                        component.copy(unstableVersion = component.unstableVersion + 1)
                    } else {
                        component.copy()
                    }
                }
        JsonHelper.writeComponentsFile(
                newComponents.map(ComponentJson.Companion::create),
                File("$currentDirectory/$COMPONENTS_JSON_FILE_PATH")
        )
    }

    private fun isComponentUstableAndChanged(component: Component, resultByFile: ComponentCheckResult, resultByConfig: ComponentCheckResult): Boolean {
        return !component.stable && (resultByFile.isComponentChanged || resultByConfig.isComponentChanged)
    }
}