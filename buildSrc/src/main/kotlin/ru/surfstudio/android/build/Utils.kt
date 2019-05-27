package ru.surfstudio.android.build

import com.beust.klaxon.Klaxon
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

fun parseComponentJson(): List<Component> {
    return Klaxon().parseArray(File(COMPONENTS_JSON_FILE_PATH))
            ?: throw RuntimeException("Can't parse components.json")
}