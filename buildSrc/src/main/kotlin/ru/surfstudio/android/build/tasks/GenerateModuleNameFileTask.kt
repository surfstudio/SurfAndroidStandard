package ru.surfstudio.android.build.tasks

import com.google.gson.GsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import java.io.File

/**
 * Task for generating data about versions to JSON file for version plugin
 * Do this task before deploy Android-Standard version plugin
 */
open class GenerateModuleNameFileTask : DefaultTask() {

    companion object {
        private const val OUTPUT_DIR = "buildSrc/"
        private const val FILE_NAME = "modules.json"
    }

    @TaskAction
    fun generate() {
        val moduleNames = (Components.value.flatMap { it.libraries }.map { it.name })
        File("$OUTPUT_DIR/$FILE_NAME").writeText(GsonBuilder().create().toJson(moduleNames))
    }
}