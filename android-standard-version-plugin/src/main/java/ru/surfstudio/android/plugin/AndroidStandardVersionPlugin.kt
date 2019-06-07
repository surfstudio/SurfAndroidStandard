package ru.surfstudio.android.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidStandardVersionPlugin : Plugin<Project> {

    companion object {
        private const val PLUGIN_NAME = "androidStandard"
    }

    override fun apply(target: Project) {
        target.extensions.create(PLUGIN_NAME, AndroidStandardVersionExtension::class.java)
    }
}