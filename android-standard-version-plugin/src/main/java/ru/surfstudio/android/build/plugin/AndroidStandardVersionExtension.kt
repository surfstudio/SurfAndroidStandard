package ru.surfstudio.android.build.plugin

import com.google.gson.GsonBuilder
import org.gradle.api.GradleException
import java.io.File

object AndroidStandardVersionExtension {

    private const val INPUT_DIR = "resources"
    private const val FILE_NAME = "versions.json"

    private val androidStandardMap: Map<String, String> by lazy {
        GsonBuilder()
            .create()
            .fromJson(File("$INPUT_DIR/$FILE_NAME").reader(), Array<LibVersion>::class.java)
            .associate { it.name to it.version }
    }


    fun version(name: String): String = androidStandardMap[name]
        ?: throw GradleException("No version associated with $name")
}