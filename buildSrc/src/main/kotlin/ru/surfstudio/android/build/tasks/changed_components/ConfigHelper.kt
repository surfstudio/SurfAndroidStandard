package ru.surfstudio.android.build.tasks.changed_components

import groovy.util.ConfigObject
import groovy.util.ConfigSlurper
import java.io.File

const val CONFIG_GRADLE_PATH = "/buildSrc/config.gradle"

/**
 * Helper for parsing config.gradle file
 */
object ConfigHelper {

    fun parseConfigFile(path: String): ConfigObject {
        val configFilePath = "$path$CONFIG_GRADLE_PATH"
        println(configFilePath)
        return ConfigSlurper().parse(File(configFilePath).readText())
    }
}