package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.changed_components.ComponentsConfigurationChecker
import ru.surfstudio.android.build.tasks.changed_components.ComponentsDiffProvider
import ru.surfstudio.android.build.tasks.changed_components.ComponentsFilesChecker
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner

const val RELEASE_NOTES_FILE_NAME = "RELEASE_NOTES.md"

open class CheckReleaseNotesChangedTask : DefaultTask() {

    private lateinit var revisionToCompare: String

    /**
     * checks and fails if component was changed (files or configuration) but its release notes file was not changed
     */
    @TaskAction
    fun check() {
        extractInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        val componentsChangeResults = ComponentsConfigurationChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
        val componentsChangeFilesResults = ComponentsFilesChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()

        val currentComponents = Components.value
        val componentsWithDiffs = ComponentsDiffProvider(currentRevision, revisionToCompare, currentComponents).provideComponentsWithDiff()

        currentComponents.forEach { component ->
            val resultByConfig = componentsChangeResults.find { it.componentName == component.name }?.isComponentChanged!!
            val resultByFile = componentsChangeFilesResults.find { it.componentName == component.name }?.isComponentChanged!!

            if (isComponentChanged(resultByConfig, resultByFile)) {
                if (isComponentHasDiffs(componentsWithDiffs, component)) {
                    val diffs = componentsWithDiffs.getValue(component)
                    if (!isReleaseFileIncluded(diffs, component.name))
                        fail("Component ${component.name} was changed but its file release notes file was not changed")
                } else {
                    fail("Component ${component.name} changed its configuration, but its file release notes file was not changed")
                }
            }
        }
    }

    private fun isComponentHasDiffs(componentsWithDiffs: Map<Component, List<String>>, component: Component) =
            componentsWithDiffs.containsKey(component)

    private fun isComponentChanged(resultByConfig: Boolean, resultByFile: Boolean) =
            resultByConfig.or(resultByFile)

    private fun isReleaseFileIncluded(diffs: List<String>, componentName: String) =
            diffs.contains("$componentName/$RELEASE_NOTES_FILE_NAME")

    private fun extractInputArguments() {
        if (!project.hasProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            throw GradleException("please specify ${GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE} param")
        }
        revisionToCompare = project.findProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
    }

    private fun fail(reason: String) {
        throw GradleException(reason)
    }
}