package ru.surfstudio.android.build.tasks.generate_release_notes_diff

import ru.surfstudio.android.build.tasks.generate_release_notes_diff.GitDiff.Type.*

/**
 * Simple parser for git diff.
 */
class SimpleGitDiffParser {

    private val ADD_CHAR = "+"
    private val REMOVE_CHAR = "-"
    private val ADD_FILE_CHAR = "+++"
    private val REMOVE_FILE_CHAR = "---"
    private val SEPARATOR_CHAR = '@'
    private val SPACE_CHAR = ' '

    private var currentChangeLineNumber = 0
    private var currentChangeLinesCount = 0
    private var addsCount = 0
    private var removesCount = 0
    private var lastDiff: GitDiff? = null

    /**
     * Parse diff string into list with [GitDiff]s
     *
     * @param diff raw diff value
     *
     * @return List with [GitDiff]
     */
    fun parse(diff: String): List<GitDiff> {
        val diffs = mutableListOf<GitDiff>()
        diff.lines().forEach { line ->
            when {
                line.startsWith(ADD_FILE_CHAR) || line.startsWith(REMOVE_FILE_CHAR) -> {
                }
                line.startsWith(SEPARATOR_CHAR) -> diffs.add(makeSeparator(line))
                line.startsWith(ADD_CHAR) -> diffs.add(makeDiff(line, ADD))
                line.startsWith(REMOVE_CHAR) -> diffs.add(makeDiff(line, REMOVE))
            }
        }
        return diffs
    }

    private fun makeSeparator(separatorLine: String): GitDiff {
        val regex = "\\d+".toRegex()
        val lineNumber = regex.find(separatorLine)?.value?.toIntOrNull() ?: 0
        currentChangeLineNumber = lineNumber + addsCount - removesCount
        return GitDiff(lineNumber.toString(), lineNumber, SEPARATE)
                .also { lastDiff = it }
    }

    private fun makeDiff(line: String, type: GitDiff.Type): GitDiff {
        setDiffsCount(type)
        setCurrentChangedLinesCount(type)
        val lineNumber = currentChangeLineNumber + currentChangeLinesCount
        return GitDiff(addSpaceAfterFirstSymbol(line), lineNumber, type)
                .also { lastDiff = it }
    }

    private fun setDiffsCount(type: GitDiff.Type) {
        when (type) {
            ADD -> addsCount++
            REMOVE -> removesCount++
            else -> {
            }
        }
    }

    private fun setCurrentChangedLinesCount(currentType: GitDiff.Type) {
        val lastType = lastDiff?.type
        when {
            //When it's just add change, we're adding one line to a current
            lastType == SEPARATE && currentType == ADD -> currentChangeLinesCount = 1
            //When type of next operation is different, we're resetting counter
            lastType != currentType -> currentChangeLinesCount = 0
            else -> currentChangeLinesCount++
        }
    }

    private fun addSpaceAfterFirstSymbol(line: String): String {
        return buildString {
            append(line.first())
            append(SPACE_CHAR)
            append(line.drop(1))
        }
    }
}