package ru.surfstudio.android.build.tasks.check_release_notes

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.RELEASE_NOTES_FILE_NAME
import ru.surfstudio.android.build.exceptions.component.ComponentNotFoundException
import ru.surfstudio.android.build.exceptions.property.PropertyNotDefineException
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesForConfigurationException
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesForFilesException
import ru.surfstudio.android.build.tasks.changed_components.ComponentsConfigurationChecker
import ru.surfstudio.android.build.tasks.changed_components.ComponentsDiffProvider
import ru.surfstudio.android.build.tasks.changed_components.ComponentsFilesChecker
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner

open class CheckReleaseNotesChangedTask : DefaultTask() {

    companion object {
        const val MD_FILE_REGEX = "/*\\.md"
        const val GRADLE_FILE_REGEX = "/*\\.gradle"
        const val SAMPLE_FILE_REGEX = "/*sample*/"
    }

    private lateinit var revisionToCompare: String

    /**
     * checks release notes file was not changed if component was changed
     *
     * @throws ReleaseNotesForConfigurationException  if component was changed in configuration but release notes was not changed
     * @throws ReleaseNotesForFilesException  if component was changed in files but release notes was not changed
     */
    @TaskAction
    fun check() {
        extractInputArguments()
        val currentRevision = GitCommandRunner().getCurrentRevisionShort()

        val componentsChangeResults = ComponentsConfigurationChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()
        val componentsChangeFilesResults = ComponentsFilesChecker(currentRevision, revisionToCompare)
                .getChangeInformationForComponents()

        val componentsWithDiffs = ComponentsDiffProvider(currentRevision, revisionToCompare, Components.value)
                .provideComponentsWithDiff()

        componentsWithDiffs.filter {
            isComponentRequiredReleaseNotes(it.value)
        }.forEach { (component, diffs) ->
            val resultByConfig = componentsChangeResults.find { it.componentName == component.name }
                    ?: throw ComponentNotFoundException(component.name)
            val resultByFile = componentsChangeFilesResults.find { it.componentName == component.name }
                    ?: throw ComponentNotFoundException(component.name)

            if (isComponentChanged(resultByConfig.isComponentChanged, resultByFile.isComponentChanged)) {
                if (!isReleaseFileIncluded(diffs, component.name)) {
                    throw ReleaseNotesForFilesException(
                            component.name,
                            resultByConfig.reasonOfComponentChange.reason
                    )
                }
            }
        }
    }

    private fun isComponentRequiredReleaseNotes(diffs: List<String>): Boolean {
        return !diffs.all {
            MD_FILE_REGEX.toRegex().containsMatchIn(it)
                    || GRADLE_FILE_REGEX.toRegex().containsMatchIn(it)
                    || SAMPLE_FILE_REGEX.toRegex().containsMatchIn(it)
        }
    }

    private fun isComponentChanged(resultByConfig: Boolean, resultByFile: Boolean) =
            resultByConfig.or(resultByFile)

    private fun isReleaseFileIncluded(diffs: List<String>, componentName: String): Boolean =
            diffs.find { it.contains("$componentName/$RELEASE_NOTES_FILE_NAME") } != null

    private fun extractInputArguments() {
        if (!project.hasProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            throw PropertyNotDefineException(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)
        }
        revisionToCompare = project.findProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
    }
}