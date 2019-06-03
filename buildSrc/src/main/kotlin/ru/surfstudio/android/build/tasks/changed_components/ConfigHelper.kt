package ru.surfstudio.android.build.tasks.changed_components

import groovy.util.ConfigObject
import groovy.util.ConfigSlurper
import java.io.File

/**
 * Helper for parsing config.gradle file
 */
object ConfigHelper {

    fun parseConfigFile(path: String): ConfigObject {
        return ConfigSlurper().parse(File(path).readText())
    }
}