package ru.surfstudio.android.plugin

import com.google.gson.GsonBuilder
import org.gradle.api.GradleException
import java.io.File

open class AndroidStandardVersionExtension {

    companion object {
        private const val FILE_NAME = "versions.json"
    }

    private val androidStandardMap: Map<String, String> by lazy {
        //mapOf("rx-extension" to "0.3.0")
        GsonBuilder()
            .create()
            .fromJson(File(FILE_NAME).reader(), Array<LibVersion>::class.java)
            .associate { it.name to it.version }
    }


    fun version(name: String): String = androidStandardMap[name]
        ?: throw GradleException("No version associated with $name")
}