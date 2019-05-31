package ru.surfstudio.android.build.tasks.changed_components

import com.beust.klaxon.Klaxon
import ru.surfstudio.android.build.model.ProjectConfiguration
import ru.surfstudio.android.build.model.json.ComponentJson
import java.io.File

/**
 * helper for parsing components.json and project info configuration files
 */
object JsonHelper {

    fun parseComponentJson(path: String): List<ComponentJson> {
        return Klaxon().parseArray(File(path))
                ?: throw RuntimeException("Can't $path")
    }

    fun writeComponentsJson(list: List<ComponentJson>, file: File) {
        file.writeText(Klaxon().toJsonString(list))
    }

    fun parseProjectConfigurationFile(path: String): ProjectConfiguration {
        return Klaxon().parse<ProjectConfiguration>(File(path))
                ?: throw RuntimeException("Can't parse $path")
    }

    fun writeProjectConfigurationJson(project: ProjectConfiguration, file: File) {
        file.writeText(Klaxon().toJsonString(project))
    }
}