package ru.surfstudio.android.build.tasks.generate_release_notes_diff

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutputFactory
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.ReleaseNotes
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.lang.RuntimeException

/**
 * Task to see the differences between two revisions of RELEASE_NOTES.md in each module of a project
 */
open class GenerateReleaseNotesDiffTask : DefaultTask() {

    private lateinit var componentName: String
    private lateinit var revisionToCompare: String
    private lateinit var currentRevision: String

    private val gitRunner: GitCommandRunner = GitCommandRunner()

    private val outputStyler = services.get(StyledTextOutputFactory::class.java).create("styled output")

    @TaskAction
    fun generate() {
        extractInputArguments()
        if (componentName.isNotEmpty()) {
            val component = Components.value.find { it.name == componentName }
                    ?: throw RuntimeException("Component cant be found")
            generateComponentDiff(component)
        } else {
            Components.value.forEach(::generateComponentDiff)
        }
    }

    private fun generateComponentDiff(component: Component) {
        val rawDiff = extractRawDiff(component)
        val diffs = parseRawDiff(rawDiff)
        if (diffs.isNotEmpty()) printComponentName(component)
        printDiff(diffs)
        if (diffs.isNotEmpty()) println()
    }

    private fun printComponentName(component: Component) {
        outputStyler.style(StyledTextOutput.Style.Header).println(component.name)
    }

    private fun parseRawDiff(diff: String): List<GitDiff> =
            SimpleGitDiffParser.parse(diff)

    private fun extractRawDiff(component: Component): String {
        val filePath = ReleaseNotes.getReleaseNotesFilePath(component)
        return gitRunner.getFullDiff(currentRevision, revisionToCompare, filePath) ?: ""
    }

    private fun printDiff(diffs: List<GitDiff>) {
        diffs.forEachIndexed { index, diff ->
            addSpaceBetweenChanges(index, diff.type)
            val style = getStyleFromDiffType(diff.type)
            outputStyler.style(style).println(diff.line)
        }
    }

    private fun addSpaceBetweenChanges(index: Int, type: GitDiff.Type) {
        if (index != 0 && type == GitDiff.Type.SEPARATE) {
            println()
        }
    }

    private fun getStyleFromDiffType(type: GitDiff.Type): StyledTextOutput.Style = when (type) {
        GitDiff.Type.ADD -> StyledTextOutput.Style.Success
        GitDiff.Type.REMOVE -> StyledTextOutput.Style.Failure
        GitDiff.Type.SEPARATE -> StyledTextOutput.Style.Normal
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