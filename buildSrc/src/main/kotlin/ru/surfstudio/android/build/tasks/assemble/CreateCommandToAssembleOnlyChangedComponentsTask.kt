package ru.surfstudio.android.build.tasks.assemble

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.tasks.changed_components.ComponentsConfigurationChecker
import ru.surfstudio.android.build.tasks.changed_components.ComponentsFilesChecker
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult
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

        val componentsWithChangedConfiguration = checkForConfigurationChanges(currentRevision)
        val componentsWithChangedFiles = checkForFileChanges(currentRevision)

        checkComponentsChanged(componentsWithChangedConfiguration + componentsWithChangedFiles)
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            throw GradleException("please specify ${GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE} param")
        }
        revisionToCompare = project.findProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
    }

    private fun checkForConfigurationChanges(currentRevision: String): List<ComponentCheckResult> {
        return ComponentsConfigurationChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
    }

    private fun checkForFileChanges(currentRevision: String): List<ComponentCheckResult> {
        return ComponentsFilesChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents(ignoreReleaseNotesChanges = true)
    }

    private fun checkComponentsChanged(changeResultComponents: List<ComponentCheckResult>) {
        val components = changeResultComponents.filter {
            it.isComponentChanged
        }

        if (components.isNotEmpty()) {
            val assembleCommand = createOutputForChangedComponents(components)

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
    }

    private fun createOutputForChangedComponents(results: List<ComponentCheckResult>): String {
        return results.map {
            ":${it.componentName}:clean :${it.componentName}:assemble"
        }.joinToString(separator = " ")
    }
}