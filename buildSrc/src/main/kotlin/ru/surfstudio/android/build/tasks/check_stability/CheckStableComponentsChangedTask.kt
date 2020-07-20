package ru.surfstudio.android.build.tasks.check_stability

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE
import ru.surfstudio.android.build.tasks.changed_components.ComponentsConfigurationChecker
import ru.surfstudio.android.build.tasks.changed_components.ComponentsFilesChecker
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult

val currentDirectory: String = System.getProperty("user.dir")

/**
 * Task checking if stable components in current revision compared to [revisionToCompare] are changed
 */
open class CheckStableComponentsChangedTask : DefaultTask() {

    private lateinit var revisionToCompare: String

    @TaskAction
    fun check() {
        extractInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        checkForFileChanges(currentRevision)

        checkForConfigurationChanges(currentRevision)
    }

    private fun checkForConfigurationChanges(currentRevision: String) {
        val componentsChangeResults = ComponentsConfigurationChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()

        println("ComponentsChangeResults:")
        println(componentsChangeResults)
        if (componentsChangeResults.isNotEmpty()) {
            checkStableComponentsChanged(componentsChangeResults)
        }
    }

    private fun checkForFileChanges(currentRevision: String) {
        val componentsChangeFilesResults = ComponentsFilesChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents(ignoreReleaseNotesChanges = true)
        println("ComponentsChangeFilesResults:")
        println(componentsChangeFilesResults)
        if (componentsChangeFilesResults.isNotEmpty()) {
            checkStableComponentsChanged(componentsChangeFilesResults)
        }
    }

    private fun extractInputArguments() {
        if (!project.hasProperty(COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            throw GradleException("please specify $COMPONENTS_CHANGED_REVISION_TO_COMPARE param")
        }
        revisionToCompare = project.findProperty(COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
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
        return results.joinToString(separator = "/n") {
            "${it.componentName} ${it.reasonOfComponentChange.reason}"
        }
    }

    private fun fail(reason: String) {
        val message = "Make following components unstable to pass this stage:\n".plus(reason)
        throw GradleException(message)
    }
}