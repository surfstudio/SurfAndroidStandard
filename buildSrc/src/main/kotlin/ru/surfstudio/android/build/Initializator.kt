package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.ComponentDirectoryNotExistException
import ru.surfstudio.android.build.exceptions.LibraryDirectoryNotExistException
import ru.surfstudio.android.build.exceptions.SampleDirectoryNotExistException
import ru.surfstudio.android.build.model.json.ComponentJson
import java.io.File
import ru.surfstudio.android.build.utils.JsonHelper

object Initializator {

    private const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

    /**
     * Parse value.json and create value
     */
    @JvmStatic
    fun init() {
        val jsonComponents = JsonHelper.parseComponentsJson(COMPONENTS_JSON_FILE_PATH)
        checkComponentsFolders(jsonComponents)
        Components.init(jsonComponents)
    }

    /**
     * Check value directories for exist
     */
    private fun checkComponentsFolders(componentJsons: List<ComponentJson>) {
        componentJsons.forEach { component ->

            //check component "dir"
            if (!File(component.dir).exists()) {
                throw ComponentDirectoryNotExistException(component.id)
            }

            //check libs
            component.libs.forEach { lib ->
                if (!File("${component.dir}/${lib.dir}").exists()) {
                    throw LibraryDirectoryNotExistException(component.id, lib.name, lib.dir)
                }
            }

            //check samples
            component.samples.forEach { sample ->
                if (!File("${component.dir}/${sample.dir}").exists()) {
                    throw SampleDirectoryNotExistException(component.id, sample.name)
                }
            }
        }
    }
}