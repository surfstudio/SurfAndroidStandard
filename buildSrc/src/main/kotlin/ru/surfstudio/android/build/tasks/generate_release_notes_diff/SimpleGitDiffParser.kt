package ru.surfstudio.android.build.tasks.generate_release_notes_diff

/**
 * Simple parser for git diff.
 */
object SimpleGitDiffParser {

    private val ADD_CHAR = "+"
    private val REMOVE_CHAR = "-"
    private val ADD_FILE_CHAR = "+++"
    private val REMOVE_FILE_CHAR = "---"
    private val SEPARATOR_CHAR = '@'

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
                line.startsWith(ADD_CHAR) -> diffs.add(makeAddDiff(line))
                line.startsWith(REMOVE_CHAR) -> diffs.add(makeRemoveDiff(line))
            }
        }
        return diffs
    }

    private fun makeAddDiff(line: String): GitDiff {
        return GitDiff(line.drop(1), GitDiff.Type.ADD)
    }

    private fun makeRemoveDiff(line: String): GitDiff {
        return GitDiff(line.drop(1), GitDiff.Type.REMOVE)
    }

    private fun makeSeparator(separatorLine: String): GitDiff {
        val regex = "\\d+".toRegex()
        val lineNumber = regex.find(separatorLine)?.value
        return GitDiff("Строка: $lineNumber", GitDiff.Type.SEPARATE)
    }
}