package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.tasks.changed_components.*
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult

val currentDirectory: String = System.getProperty("user.dir")

/**
 * Task checking if stable components in current revision compared to [revisionToCompare] are changed
 */
open class CheckStableComponentsChangedTask : DefaultTask() {

    private lateinit var revisionToCompare: String

    @TaskAction
    fun check() {
        println("$CHECK_STABLE_COMPONENTS_TASK_NAME started")

        extractInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        checkForFileChanges(currentRevision)

        checkForConfigurationChanges(currentRevision)

        println("$CHECK_STABLE_COMPONENTS_TASK_NAME ended")
    }

    private fun checkForConfigurationChanges(currentRevision: String) {
        val componentsChangeResults = ComponentsConfigChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
        if (componentsChangeResults.isNotEmpty()) {
            checkStableComponentsChanged(componentsChangeResults)
        }
    }

    private fun checkForFileChanges(currentRevision: String) {
        val componentsChangeFilesResults = ComponentsFilesChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
        if (componentsChangeFilesResults.isNotEmpty()) {
            checkStableComponentsChanged(componentsChangeFilesResults)
        }
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(REVISION_TO_COMPARE)) throw GradleException("please specify $REVISION_TO_COMPARE param")
        revisionToCompare = project.findProperty(REVISION_TO_COMPARE) as String
    }

    private fun checkStableComponentsChanged(changeResultComponents: List<ComponentCheckResult>) {
        val components = changeResultComponents.filter {
            it.isComponentChanged && it.isComponentStable
        }
        if (components.isNotEmpty()) {
            fail(createOutputForChangedComponents(components))
        }
    }

    private fun createOutputForChangedComponents(results: List<ComponentCheckResult>): String {
        return results.map {
            "${it.componentName}  ${it.reasonOfComponentChange.reason}"
        }.joinToString(separator = "/n")
    }

    private fun fail(reason: String) {
        throw GradleException(reason)
    }
}