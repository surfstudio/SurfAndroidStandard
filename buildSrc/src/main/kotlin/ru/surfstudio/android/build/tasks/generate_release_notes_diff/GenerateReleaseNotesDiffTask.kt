package ru.surfstudio.android.build.tasks.generate_release_notes_diff

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.ReleaseNotes
import ru.surfstudio.android.build.exceptions.ComponentNotFoundException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File

/**
 * Task to see the differences between two revisions of RELEASE_NOTES.md in each module of a project
 */
open class GenerateReleaseNotesDiffTask : DefaultTask() {

    private lateinit var componentName: String
    private lateinit var revisionToCompare: String
    private lateinit var currentRevision: String

    private val gitRunner: GitCommandRunner = GitCommandRunner()
    private var i = 0

    @TaskAction
    fun generate() {
        extractInputArguments()
        if (componentName.isNotEmpty()) {
            val component = findComponent()
            generateChangedReleaseNotesComponentName(component)
        } else {
            Components.value.forEach(::generateChangedReleaseNotesComponentName)
        }
    }

    private fun findComponent(): Component =
            Components.value.find { it.name == componentName }
                    ?: throw ComponentNotFoundException(componentName)

    private fun generateChangedReleaseNotesComponentName(component: Component) {
        val rawDiff = getChangedReleaseNotesComponent(component)
        writeToFile(rawDiff.replace("/RELEASE_NOTES.md", "".trim()))
    }

    private fun writeToFile(text: String) {
        File("buildSrc/releaseNotesDiff.txt").appendText("$text")
    }

    private fun getChangedReleaseNotesComponent(component: Component): String {
        val filePath = ReleaseNotes.getReleaseNotesFilePath(component)
        return gitRunner.getFullDiff(currentRevision, revisionToCompare, filePath) ?: ""
    }

    private fun extractInputArguments() {
        componentName = if (!project.hasProperty(GradleProperties.COMPONENT)) {
            EMPTY_STRING
        } else {
            project.findProperty(GradleProperties.COMPONENT) as String
        }

        revisionToCompare = if (!project.hasProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            EMPTY_STRING
        } else {
            project.findProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
        }

        currentRevision = if (project.hasProperty(GradleProperties.CURRENT_REVISION)) {
            project.findProperty(GradleProperties.CURRENT_REVISION) as String
        } else {
            gitRunner.getCurrentRevisionShort()
        }
    }
}