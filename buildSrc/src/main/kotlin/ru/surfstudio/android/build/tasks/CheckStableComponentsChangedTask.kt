package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.model.CheckComponentChangedResult
import ru.surfstudio.android.build.tasks.changed_components.ComponentsConfigChecker
import ru.surfstudio.android.build.tasks.changed_components.ComponentsFilesChecker
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner


const val REVISION_TO_COMPARE = "revisionToCompare"
val currentDirectory: String = System.getProperty("user.dir")

/**
 * Task checking if stable components in current revision compared to [revisionToCompare] are changed
 */
open class CheckStableComponentsChanged : DefaultTask() {

    private val TASK_NAME = "CheckStableComponentsChanged: "
    private lateinit var revisionToCompare: String

    @TaskAction
    fun check() {
        println("$TASK_NAME started")

        getInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        checkForFileChanges(currentRevision)

        checkForConfigurationChanges(currentRevision)

        println("$TASK_NAME ended")
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

    private fun getInputArguments() {
        if (!project.hasProperty(REVISION_TO_COMPARE)) throw GradleException("please specify $REVISION_TO_COMPARE param")
        revisionToCompare = project.findProperty(REVISION_TO_COMPARE) as String
    }

    private fun checkStableComponentsChanged(changeResultComponents: List<CheckComponentChangedResult>) {
        val components = changeResultComponents.filter {
            it.isComponentChanged && it.isComponentStable
        }
        if (components.isNotEmpty()) {
            fail(createOutputForChangedComponents(components))
        }
    }

    private fun createOutputForChangedComponents(results: List<CheckComponentChangedResult>): String {
        return results.map {
            "${it.componentName}  ${it.reasonOfComponentChange.reason}"
        }.joinToString(separator = "/n")
    }

    private fun fail(reason: String) {
        throw GradleException(reason)
    }
}