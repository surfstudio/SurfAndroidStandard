package ru.surfstudio.android.plugin

import com.google.gson.GsonBuilder
import org.gradle.api.GradleException

/**
 * Extension for version plugin
 */
open class AndroidStandardVersionExtension {

    companion object {
        private const val FILE_NAME = "versions.json"
    }

    private val androidStandardMap: Map<String, String> by lazy {
        GsonBuilder()
            .create()
            .fromJson(javaClass.classLoader.getResourceAsStream(FILE_NAME).reader(), Array<LibVersion>::class.java)
            .associate { it.first to it.second }
    }

    fun version(name: String): String = androidStandardMap[name]
            ?: throw GradleException("No Android-Standard version associated with $name")

}