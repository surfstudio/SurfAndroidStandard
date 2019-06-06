package ru.surfstudio.android.build.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

open class AndroidStandardVersionPlugin : Plugin<Project> {

    companion object {
        private const val PLUGIN_NAME = "androidStandardVersion"
    }

    override fun apply(target: Project) {
        target.extensions.create(PLUGIN_NAME, AndroidStandardVersionExtension::class.java, target)
    }
}