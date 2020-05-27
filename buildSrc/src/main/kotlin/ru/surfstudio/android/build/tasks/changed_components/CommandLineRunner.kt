package ru.surfstudio.android.build.tasks.changed_components

import org.gradle.api.GradleException
import java.io.File
import java.util.concurrent.TimeUnit

private const val COMMAND_RUN_TIMEOUT_SEC = 300L

/**
 * Runs command in command line
 */
object CommandLineRunner {

    /**
     * run command and get result
     *
     * @param command running command
     * @param workingDir current working directoryPath
     *
     * @return result of command
     */
    fun runCommandWithResult(command: String, workingDir: File): String? {
        val process = runCommandProcess(command, workingDir)
        process.waitFor(COMMAND_RUN_TIMEOUT_SEC, TimeUnit.SECONDS)
        if (process.exitValue() != 0) {
            throw GradleException(process.errorStream.bufferedReader().readText())
        }

        return process.inputStream.bufferedReader().readText()
    }

    /**
     * Function which checks if the command executed successfully
     *
     * @param command running command
     * @param workingDir current working directoryPath
     *
     * @return true if the command executed successfully
     */
    fun isCommandSucceed(command: String, workingDir: File): Boolean {
        val process = runCommandProcess(command, workingDir)
        process.waitFor(COMMAND_RUN_TIMEOUT_SEC, TimeUnit.SECONDS)
        return process.exitValue() == 0
    }

    private fun runCommandProcess(command: String, workingDir: File): Process {
        val parts = command.split(SPLIT_STRING.toRegex()).filter { it.isNotBlank() }
        return ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()
    }
}