package ru.surfstudio.android.build.tasks.assemble

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.changed_components.ComponentsConfigurationChecker
import ru.surfstudio.android.build.tasks.changed_components.ComponentsFilesChecker
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import java.io.File

/**
 * Find changed components and create command to assemble only changed
 */
open class CreateCommandToAssembleOnlyChangedComponentsTask : DefaultTask() {

    companion object {
        const val ASSEMBLE_COMMAND_FILE_URL = "buildSrc/build/tmp/assembleCommand.txt"
    }

    private lateinit var revisionToCompare: String

    @TaskAction
    fun check() {
        extractInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        val currentComponents = Components.value

        val componentsChangeResults = ComponentsConfigurationChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
                .filter { componentCheckResult -> componentCheckResult.isComponentChanged }
                .map { componentCheckResult -> currentComponents.find { it.name == componentCheckResult.componentName } }

        val componentsChangeFilesResults = ComponentsFilesChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
                .filter { componentCheckResult -> componentCheckResult.isComponentChanged }
                .map { componentCheckResult -> currentComponents.find { it.name == componentCheckResult.componentName } }

        writeAssembleCommand(componentsChangeResults + componentsChangeFilesResults)
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            throw GradleException("please specify ${GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE} param")
        }
        revisionToCompare = project.findProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
    }

    private fun writeAssembleCommand(changedComponents: List<Component?>) {
        val assembleCommand = createOutputForChangedComponents(changedComponents)

        val file = File(ASSEMBLE_COMMAND_FILE_URL)
        with(file) {
            if (exists()) {
                delete()
            }
            createNewFile()
            appendText(assembleCommand)
        }

        logger.lifecycle(assembleCommand)
    }

    private fun createOutputForChangedComponents(results: List<Component?>): String {
        return results.joinToString(separator = " ") { component ->

            val librariesAssembleCommand = component?.libraries?.joinToString(separator = " ") { library ->
                ":${library.name}:clean :${library.name}:assemble"
            }

            val samplesAssembleCommand = component?.samples?.joinToString(separator = " ") { sample ->
                ":${sample.name}:clean :${sample.name}:assemble"
            }

            return@joinToString "$librariesAssembleCommand $samplesAssembleCommand"
        }
    }
}