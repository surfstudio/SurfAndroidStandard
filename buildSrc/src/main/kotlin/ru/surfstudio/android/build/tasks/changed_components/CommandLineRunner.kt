package ru.surfstudio.android.build.tasks.changed_components

import org.gradle.api.GradleException
import java.io.File
import java.util.concurrent.TimeUnit

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
        val parts = command.split(SPLIT_STRING.toRegex())
        val process = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        process.waitFor(300, TimeUnit.SECONDS)
        if (process.exitValue() != 0) {
            throw GradleException(process.errorStream.bufferedReader().readText())
        }

        return process.inputStream.bufferedReader().readText()
    }
}