package ru.surfstudio.android.build.tasks.changed_components

import groovy.util.ConfigObject
import groovy.util.ConfigSlurper
import ru.surfstudio.android.build.Folders.CONFIG_GRADLE_PATH
import java.io.File

/**
 * Helper for parsing config.gradle file
 */
object ConfigHelper {

    fun parseConfigFile(path: String): ConfigObject {
        val configFilePath = "$path$CONFIG_GRADLE_PATH"

        return ConfigSlurper().parse(File(configFilePath).readText())
    }
}