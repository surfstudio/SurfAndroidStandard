package ru.surfstudio.android.build

import com.beust.klaxon.Klaxon
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

fun configurateComponents() {
    val components = parseComponentJson()
    checkComponentsFolders(components)
}

private fun parseComponentJson(): List<Component> {
    return Klaxon().parseArray(File(COMPONENTS_JSON_FILE_PATH))
            ?: throw RuntimeException("Can't parse components.json")
}

private fun checkComponentsFolders(components: List<Component>) {
    components.forEach { component ->
        //check "dir"
        if (!File(component.dir).exists()) {
            throw RuntimeException(
                    "Component ${component.id} doesn't have existing directory. " +
                            "Please, check components.json and create folder with 'dir' name."
            )
        }

        //check libs
        component.libs.forEach { lib ->
            if (!File("${component.dir}/${lib.dir}").exists()) {
                throw RuntimeException(
                        "Component ${component.id} with library ${lib.artifactName} doesn't " +
                                "have existing directory ${lib.dir}. Please, check components.json" +
                                " and create folder with 'dir' name."
                )
            }
        }

        //check samples
        component.samples.forEach { sample ->
            if (!File("${component.dir}/${sample}").exists()) {
                throw RuntimeException(
                        "Component ${component.id} has sample $sample, but folder doesn't exist."
                )
            }
        }
    }
}