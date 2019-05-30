package ru.surfstudio.android.build.tasks.check_components

import org.gradle.api.GradleException
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Class for running git command
 */
class GitCommandRunner(
        val directory: String = currentDirectory
) {
    private val GIT_DIFF_COMMAND = "git diff --no-commit-id --name-only"
    private val GIT_CHECKOUT_COMMAND = "git checkout "
    private val GIT_GET_CURRENT_REVISION_COMMAND = "git rev-parse --short HEAD"
    private val SPLIT_STRING = "\\s"


    fun diff(firstRevision: String, secondRevision: String): List<String>? {
        val command = "$GIT_DIFF_COMMAND $firstRevision $secondRevision"
        val res = runCommandWithResult(command, File(directory))
        return res?.split(SPLIT_STRING.toRegex())
    }

    fun checkoutRevision(revision: String) {
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
            throw GradleException("Error running console command $command")
            null
        }
    }
}