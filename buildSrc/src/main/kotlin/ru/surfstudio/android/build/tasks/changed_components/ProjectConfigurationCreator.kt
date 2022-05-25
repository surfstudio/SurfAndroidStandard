package ru.surfstudio.android.build.tasks.changed_components

import groovy.util.ConfigObject
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.ConfigInfoProvider.currentDirectory
import ru.surfstudio.android.build.Folders.BUILD_FOLDER_NAME
import ru.surfstudio.android.build.Folders.BUILD_OUTPUT_FOLDER_PATH
import ru.surfstudio.android.build.Folders.OUTPUT_JSON_FOLDER_PATH
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.tasks.changed_components.models.ComponentWithVersion
import ru.surfstudio.android.build.tasks.changed_components.models.DependencyWithVersion
import ru.surfstudio.android.build.tasks.changed_components.models.LibraryWithVersion
import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration
import ru.surfstudio.android.build.utils.COMPONENTS_JSON_FILE_PATH
import ru.surfstudio.android.build.utils.JsonHelper
import java.io.File

/**
 * Class for creating information about project configuration. Writes information to [outputJsonFileName]
 *
 * @param revision reviosion of the project the configuration file is created for
 * @param pathToProject path to the project the configuration file is created for
 */
class ProjectConfigurationCreator(
        private val revision: String,
        private val pathToProject: String
) {

    private val outputJsonFolderPath = "$currentDirectory/$OUTPUT_JSON_FOLDER_PATH"
    private val buildOutputFolderPath = "$currentDirectory/$BUILD_OUTPUT_FOLDER_PATH"
    private val outputJsonFileName = "$outputJsonFolderPath$revision.json"

    fun createProjectConfigurationFile() {
        saveProjectConfigurationToFile(createProjectConfiguration())
    }

    /**
     * Creates configuration info file for project
     *
     * @return project configuration
     */
    private fun createProjectConfiguration(): ProjectConfiguration {
        val config = ConfigHelper.parseConfigFile(pathToProject)
        val configList = config.getValue("ext") as ConfigObject
        val versions = configList["libraryVersions"] as LinkedHashMap<String, String>
        val libMinSdkVersion = configList["libMinSdkVersion"] as Int
        val targetSdkVersion = configList["targetSdkVersion"] as Int
        val moduleVersionCode = configList["moduleVersionCode"] as Int
        val compileSdkVersion = configList["compileSdkVersion"] as Int


        val componentsWithVersions = createComponentsWithVersions(versions)

        return ProjectConfiguration(
                revision,
                componentsWithVersions,
                libMinSdkVersion,
                targetSdkVersion,
                moduleVersionCode,
                compileSdkVersion
        )
    }

    /**
     * Maps every component from [Components.value] to ComponentWithVersion finding version for every library in [versions]
     *
     * @param versions LinkedHashMap with pairs. Every pair contains name of library as first parameter
     * and its version as second parameter
     */
    private fun createComponentsWithVersions(versions: LinkedHashMap<String, String>): List<ComponentWithVersion> {
        val components = JsonHelper.parseComponentsJson("$pathToProject/$COMPONENTS_JSON_FILE_PATH").map(ComponentJson::transform)

        return components.map { component ->
            val libs = component.libraries.map { lib ->
                val standardDependencies = lib.androidStandardDependencies.map { dep ->
                    DependencyWithVersion(dep.name, versions[dep.name] ?: "")
                }
                val thirdPartyDependencies = lib.thirdPartyDependencies.map { dep ->
                    DependencyWithVersion(dep.name, versions[dep.name] ?: "")
                }
                LibraryWithVersion(lib.name, lib.directoryPath, thirdPartyDependencies, standardDependencies)
            }
            ComponentWithVersion(component.name, component.directory, component.baseVersion, component.stable, libs)
        }
    }

    /**
     * saves project configuration to file [outputJsonFileName]
     *
     *@param projectConfiguration configuration of project
     */
    private fun saveProjectConfigurationToFile(projectConfiguration: ProjectConfiguration) {
        createIntermediateFoldersForJsonFile()
        JsonHelper.write(projectConfiguration, createJsonOutputFile())
    }

    /**
     * if [outputJsonFileName] exists removes it and creates a new one
     *
     * @return created file
     */
    private fun createJsonOutputFile(): File {
        val fileOutputJson = File(outputJsonFileName)
        if (fileOutputJson.exists()) {
            fileOutputJson.delete()
        }
        fileOutputJson.createNewFile()

        return fileOutputJson
    }

    /**
     * checks whether intermediate folders [OUTPUT_FOLDER_NAME] and [outputJsonFolderPath] exists and if no creates them
     */
    private fun createIntermediateFoldersForJsonFile() {
        val folderBuild = File(BUILD_FOLDER_NAME)
        if (!folderBuild.exists()) folderBuild.mkdir()

        val folderOutputs = File(buildOutputFolderPath)
        if (!folderOutputs.exists()) folderOutputs.mkdir()

        val folderCurrentTask = File(outputJsonFolderPath)
        if (!folderCurrentTask.exists()) folderCurrentTask.mkdir()
    }
}