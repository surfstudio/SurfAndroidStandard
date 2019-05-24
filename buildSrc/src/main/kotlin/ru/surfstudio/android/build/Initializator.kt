package ru.surfstudio.android.build

import com.beust.klaxon.Klaxon
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

private const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

class Initializator {
    companion object {
        var components: List<Component> = emptyList()
    }

    init {
        components = parseComponentJson()
    }

    fun getModulesInfo(): List<Pair<String, String>> {
        val names = ArrayList<Pair<String, String>>()
        components.forEach { component ->
            component.libs.forEach { lib ->
                names.add(":${lib.name}" to "${component.dir}/${lib.dir}")
            }
            component.samples.forEach { sample ->
                names.add(":${sample.name}" to "${component.dir}/${sample.dir}")
            }
        }
        return names
    }

    private fun parseComponentJson(): List<Component> {
        return Klaxon().parseArray(File(COMPONENTS_JSON_FILE_PATH))
                ?: throw RuntimeException("Can't parse components.json")
    }
}
