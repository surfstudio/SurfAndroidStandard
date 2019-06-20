package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.ComponentDirectoryNotExistException
import ru.surfstudio.android.build.exceptions.ComponentNotFoundException
import ru.surfstudio.android.build.exceptions.LibraryDirectoryNotExistException
import ru.surfstudio.android.build.exceptions.SampleDirectoryNotExistException
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.utils.COMPONENTS_JSON_FILE_PATH
import ru.surfstudio.android.build.utils.JsonHelper
import java.io.File

object Initializator {

    /**
     * Parse value.json and create value
     */
    @JvmStatic
    fun init() {
        val jsonComponents = JsonHelper.parseComponentsJson(COMPONENTS_JSON_FILE_PATH)
        if (GradlePropertiesManager.isCurrentComponentAMirror()) {
            checkOnlyMirrorComponentFolder(jsonComponents)
        } else {
            checkAllComponentsFolders(jsonComponents)
        }
        Components.init(jsonComponents)
    }

    private fun checkOnlyMirrorComponentFolder(jsonComponents: List<ComponentJson>) {
        val componentMirrorName = GradlePropertiesManager.getMirrorComponentName()
        val component = jsonComponents.firstOrNull { it.id == componentMirrorName }
                ?: throw ComponentNotFoundException(componentMirrorName)
        checkComponentFolders(component)
    }

    /**
     * Check value directories for exist
     */
    private fun checkAllComponentsFolders(componentJsons: List<ComponentJson>) {
        componentJsons.forEach { component ->
            checkComponentFolders(component)
        }
    }

    private fun checkComponentFolders(component: ComponentJson) {
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