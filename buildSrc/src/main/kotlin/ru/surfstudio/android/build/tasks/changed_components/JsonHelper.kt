package ru.surfstudio.android.build.tasks.changed_components

import com.beust.klaxon.Klaxon
import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration
import java.io.File

/**
 * helper for parsing info configuration files
 */
object JsonHelper {

    fun parseProjectConfigurationFile(path: String): ProjectConfiguration {
        return Klaxon().parse<ProjectConfiguration>(File(path))
                ?: throw RuntimeException("Can't parse $path")
    }

    fun writeProjectConfigurationFile(project: ProjectConfiguration, file: File) {
        file.writeText(Klaxon().toJsonString(project))
    }
}