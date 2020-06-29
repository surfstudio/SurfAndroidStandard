package ru.surfstudio.android.build.tasks.generate_version_file

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.ConfigInfoProvider
import java.io.File

private const val PROJECT_CONFIGURATION_VERSION_FILE_NAME = "buildSrc/build/tmp/projectVersion.txt"

/**
 * Task which generates a file which an actual full project configuration version
 */
open class GenerateProjectConfigurationVersionFileTask : DefaultTask() {

    @TaskAction
    fun generate() {
        val currentVersion = ConfigInfoProvider.getVersion()

        with(File(PROJECT_CONFIGURATION_VERSION_FILE_NAME)) {
            if (exists()) {
                delete()
            }
            createNewFile()
            appendText(currentVersion)
        }

        println("Configuration file was generated with current project version: $currentVersion")
    }
}