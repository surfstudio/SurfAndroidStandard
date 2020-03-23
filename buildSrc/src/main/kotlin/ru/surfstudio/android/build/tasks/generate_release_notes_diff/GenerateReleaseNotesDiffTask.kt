package ru.surfstudio.android.build.tasks.generate_release_notes_diff

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutputFactory
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.ReleaseNotes
import ru.surfstudio.android.build.exceptions.component.ComponentNotFoundException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import ru.surfstudio.android.build.utils.EMPTY_STRING

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
            val component = findComponent()
            generateComponentDiff(component)
        } else {
            Components.value.forEach(::generateComponentDiff)
        }
    }

    private fun findComponent(): Component =
            Components.value.find { it.name == componentName }
                    ?: throw ComponentNotFoundException(componentName)

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
            SimpleGitDiffParser().parse(diff)

    private fun extractRawDiff(component: Component): String {
        val filePath = ReleaseNotes.getReleaseNotesFilePath(component)
        return gitRunner.getFullDiff(currentRevision, revisionToCompare, filePath) ?: ""
    }

    private fun printDiff(diffs: List<GitDiff>) {
        var prev: GitDiff? = null
        diffs.forEach { diff ->
            printLine(diff, prev)
            prev = diff
        }
    }

    /**
     * Prints styled line from git diff
     */
    private fun printLine(diff: GitDiff, prev: GitDiff?) {
        val style = getStyleFromDiffType(diff.type)
        val paddingSpaces = getSpaces(diff.lineNumber)
        val lineToPrint = when {
            prev == null -> return
            diff.type == GitDiff.Type.SEPARATE -> "..."
            else -> "${diff.lineNumber}$paddingSpaces${diff.line}"

        }
        outputStyler.style(style).println(lineToPrint)
    }

    private fun getStyleFromDiffType(type: GitDiff.Type): StyledTextOutput.Style = when (type) {
        GitDiff.Type.ADD -> StyledTextOutput.Style.Success
        GitDiff.Type.REMOVE -> StyledTextOutput.Style.Failure
        GitDiff.Type.SEPARATE -> StyledTextOutput.Style.Normal
    }

    /**
     * Simple padding method which adds spaces according to line length
     */
    private fun getSpaces(currentLine: Int): String {
        val space = " "
        val spacesCount = when {
            currentLine / 10 == 0 -> 3
            currentLine / 100 == 0 -> 2
            else -> 1
        }
        return space.repeat(spacesCount)
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