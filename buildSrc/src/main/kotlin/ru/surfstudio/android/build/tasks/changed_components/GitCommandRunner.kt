package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.currentDirectory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

const val GIT_DIFF_COMMAND = "git diff --no-commit-id --name-only"
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

    fun diff(firstRevision: String, secondRevision: String): List<String>? {
        val command = "$GIT_DIFF_COMMAND $firstRevision $secondRevision"
        val res = runCommandWithResult(command, File(directory))
        return res?.split(SPLIT_STRING.toRegex())
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

    private fun runCommandWithResult(command: String, workingDir: File): String? {
        return try {
            val parts = command.split(SPLIT_STRING.toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                    .directory(workingDir)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            proc.inputStream.bufferedReader().readText()
        } catch (e: IOException) {
            throw e
            null
        }
    }
}