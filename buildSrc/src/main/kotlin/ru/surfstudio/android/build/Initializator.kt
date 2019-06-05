package ru.surfstudio.android.build

import com.google.gson.GsonBuilder
import ru.surfstudio.android.build.model.json.ComponentJson
import java.io.File

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
        return GsonBuilder()
                .create()
                .fromJson(File(COMPONENTS_JSON_FILE_PATH).reader(), Array<ComponentJson>::class.java)
                .toList()
    }

    /**
     * Check value directories for exist
     */
    private fun checkComponentsFolders(componentJsons: List<ComponentJson>) {
        componentJsons.forEach { component ->

            //check component "dir"
            if (!File(component.dir).exists()) {
                throw RuntimeException(
                        "Component ${component.id} doesn't have existing directoryPath. " +
                                "Please, check value.json and create directory with 'dir' name."
                )
            }

            //check libs
            component.libs.forEach { lib ->
                if (!File("${component.dir}/${lib.dir}").exists()) {
                    throw RuntimeException(
                            "Component ${component.id} with library ${lib.name} doesn't " +
                                    "have existing directoryPath ${lib.dir}. Please, check value.json" +
                                    " and create directory with 'dir' name."
                    )
                }
            }

            //check samples
            component.samples.forEach { sample ->
                if (!File("${component.dir}/${sample.dir}").exists()) {
                    throw RuntimeException(
                            "Component ${component.id} has sample $${sample.name}, but directory doesn't exist."
                    )
                }
            }
        }
    }
}