package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.changed_components.CommandLineRunner.runCommandWithResult
import ru.surfstudio.android.build.tasks.currentDirectory
import java.io.File

const val GIT_DIFF_COMMAND = "git diff --name-only"
//const val GIT_FULL_DIFF_COMMAND = "git diff"
const val GIT_FULL_DIFF_COMMAND = "git diff -w (--ignore-all-space)"
const val GIT_CHECKOUT_COMMAND = "git checkout"
const val GIT_STASH_COMMAND = "git stash"
const val GIT_GET_CURRENT_REVISION_COMMAND = "git rev-parse --short HEAD"
const val SPLIT_STRING = "\\s"

/**
 * Class for running git command
 */
class GitCommandRunner(
        private val directory: String = currentDirectory
) {

    /**
     * Get file names with changes between current revision and revision stated in parameters
     *
     * @param rev prior revision
     *
     * @return List of file names
     */
    fun diff(rev: String): List<String>? {
        val command = "$GIT_DIFF_COMMAND $rev"
        val res = runCommandWithResult(command, File(directory))

        return res?.trim()?.split(SPLIT_STRING.toRegex())
    }

    /**
     * Show full diff between two commits as a string
     *
     * @param currentRevision   commit, which is used as a head to this diff
     * @param previousRevision  commit, which is prior to current
     * @param filePath          path to file or directory to see a diff
     * @param opts              formatting options, default - show only diff, without original text
     *
     * @return String with git diff
     */
    fun getFullDiff(
            currentRevision: String,
            previousRevision: String,
            filePath: String,
            opts: String = "-U0"
    ): String? {
        val command = if (previousRevision.isEmpty()) {
            "$GIT_FULL_DIFF_COMMAND $opts $currentRevision $filePath"
        } else {
            "$GIT_FULL_DIFF_COMMAND $opts $previousRevision $currentRevision $filePath"
        }
        return runCommandWithResult(command, File(directory))
    }

    fun checkoutRevision(revision: String) {
        runCommandWithResult(GIT_STASH_COMMAND, File(directory))
        val command = "$GIT_CHECKOUT_COMMAND $revision"
        runCommandWithResult(command, File(directory))
    }

    fun getCurrentRevisionShort(): String {
        val command = "$GIT_GET_CURRENT_REVISION_COMMAND"
        val res = runCommandWithResult(command, File(directory))

        return res?.trim().toString()
    }
}