package ru.surfstudio.android.build

import com.beust.klaxon.Klaxon
import ru.surfstudio.android.build.model.json.ComponentJson
import java.io.File
import java.lang.RuntimeException

object Initializator {

    private const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"
    private const val DEFAULT_VERSION_NAME_KEY = "defaultVersionName"

    /**
     * Parse value.json and create value
     */
    @JvmStatic
    fun init() {
        val jsonComponents = parseComponentJson()
        checkComponentsFolders(jsonComponents)
        Components.init(jsonComponents)
    }

    /**
     * Parsing value.json file
     * @return list of json value
     */
    private fun parseComponentJson(): List<ComponentJson> {
        return Klaxon().parseArray(File(COMPONENTS_JSON_FILE_PATH))
                ?: throw RuntimeException("Can't parse value.json")
    }

    /**
     * Check value directories for exist
     */
    private fun checkComponentsFolders(componentJsons: List<ComponentJson>) {
        componentJsons.forEach { component ->

            //check component "dir"
            if (!File(component.dir).exists()) {
                throw RuntimeException(
                        "Component ${component.id} doesn't have existing directory. " +
                                "Please, check value.json and create folder with 'dir' name."
                )
            }

            //check libs
            component.libs.forEach { lib ->
                if (!File("${component.dir}/${lib.dir}").exists()) {
                    throw RuntimeException(
                            "Component ${component.id} with library ${lib.name} doesn't " +
                                    "have existing directory ${lib.dir}. Please, check value.json" +
                                    " and create folder with 'dir' name."
                    )
                }
            }

            //check samples
            component.samples.forEach { sample ->
                if (!File("${component.dir}/${sample.dir}").exists()) {
                    throw RuntimeException(
                            "Component ${component.id} has sample $${sample.name}, but folder doesn't exist."
                    )
                }
            }
        }
    }
}