package ru.surfstudio.android.build.tasks.generate_release_notes_diff

/**
 * Simple git diff model. Contains information about single line differences.
 *
 * @param line text of the line, which is affected by diff
 * @param type type of git difference
 */
data class GitDiff(
        val line: String,
        val type: Type
) {

    /**
     * Git diff type for a single line
     *
     * @property ADD the line was added
     * @property REMOVE the line was removed
     * @property SEPARATE the line is used to separate two diff blocks
     *
     */
    enum class Type {
        ADD,
        REMOVE,
        SEPARATE
    }
}