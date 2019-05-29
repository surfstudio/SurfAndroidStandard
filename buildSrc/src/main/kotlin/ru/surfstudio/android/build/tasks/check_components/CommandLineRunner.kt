package ru.surfstudio.android.build.tasks.check_components

import org.gradle.api.GradleException
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class CommandLineRunner {
    private val SPLIT_STRING = "\\s"

    fun runCommandWithResult(command : String, workingDir: File): String? {
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
            throw GradleException()
            null
        }
    }
}