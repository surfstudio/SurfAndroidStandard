package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.component.ComponentDirectoryNotExistException
import ru.surfstudio.android.build.exceptions.component.ComponentNotFoundException
import ru.surfstudio.android.build.exceptions.library.LibraryDirectoryNotExistException
import ru.surfstudio.android.build.exceptions.SampleDirectoryNotExistException
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.utils.COMPONENTS_JSON_FILE_PATH
import ru.surfstudio.android.build.utils.JsonHelper
import java.io.File

object Initializator {
    /**
     * Parse value.json and create value
     * Added specifying current directory because of developed possibility to include android standard locally
     *
     * @param currentBuildDirectory directory of current setting project file
     */
    @JvmStatic
    fun init(currentBuildDirectory: String) {
        initConfigProviderWithCurrentDirectory(currentBuildDirectory)
        val jsonComponents = JsonHelper.parseComponentsJson("$currentBuildDirectory/$COMPONENTS_JSON_FILE_PATH")
        checkAllComponentsFolders(jsonComponents, currentBuildDirectory)
        Components.init(jsonComponents)
    }

    /**
     * set current directory for configuration provider
     */
    private fun initConfigProviderWithCurrentDirectory(currentDirectory: String) {
        ConfigInfoProvider.currentDirectory = "$currentDirectory/"
    }

    private fun getComponentJson(
            componentName: String,
            jsonComponents: List<ComponentJson>
    ): ComponentJson =
            jsonComponents.firstOrNull { it.id == componentName }
                    ?: throw ComponentNotFoundException(componentName)

    /**
     * Check value directories for existance
     */
    private fun checkAllComponentsFolders(componentJsons: List<ComponentJson>, currentDirectory: String) {
        componentJsons.forEach { component ->
            checkComponentFolders(component, currentDirectory)
        }
    }

    private fun checkComponentFolders(component: ComponentJson, currentDirectory: String) {
        //check component "dir"
        if (!File("$currentDirectory/${component.dir}").exists()) {
            throw ComponentDirectoryNotExistException(component.id)
        }

        //check libs
        component.libs.forEach { lib ->
            if (!File("$currentDirectory/${component.dir}/${lib.dir}").exists()) {
                throw LibraryDirectoryNotExistException(component.id, lib.name, lib.dir)
            }
        }

        //check samples
        component.samples.forEach { sample ->
            if (!File("$currentDirectory/${component.dir}/${sample.dir}").exists()) {
                throw SampleDirectoryNotExistException(component.id, sample.name)
            }
        }
    }
}