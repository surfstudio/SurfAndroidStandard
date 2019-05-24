package ru.surfstudio.android.build

import com.beust.klaxon.Klaxon
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

/**
 * Parsing component.json file
 */
fun parseComponentJson(): List<Component> {
    return Klaxon().parseArray(File(COMPONENTS_JSON_FILE_PATH))
            ?: throw RuntimeException("Can't parse components.json")
}