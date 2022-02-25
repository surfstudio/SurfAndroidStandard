package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.Folders.OUTPUT_JSON_FOLDER_PATH
import ru.surfstudio.android.build.Folders.TEMP_FOLDER_NAME
import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration
import ru.surfstudio.android.build.tasks.check_stability.currentDirectory
import ru.surfstudio.android.build.utils.JsonHelper
import java.io.File

/**
 * Provides project configuration
 *
 * @param [currentRevision] revision of project to provide configuration for
 * @param [revisionToCompare] directoryPath of the project to provide configuration for
 */
class ProjectConfigurationProvider(
        private val currentRevision: String,
        private val revisionToCompare: String
) {

    private val outputJsonDirectory = "$currentDirectory/$OUTPUT_JSON_FOLDER_PATH"
    private val tempDirectory = "$currentDirectory/$TEMP_FOLDER_NAME/"

    /**
     * provides project configuration for current revision. If project configuration file doesn`t exists creates it
     * else parses information from existing
     *
     * @return project configuration
     */
    fun provideCurrentRevisionConfiguration(): ProjectConfiguration {
        val outputJsonFile = createJsonFileNameByRevision(currentRevision)
        if (!isProjectConfigurationJsonExists(File(outputJsonFile))) {
            ProjectConfigurationCreator(currentRevision, currentDirectory).createProjectConfigurationFile()
        }

        return JsonHelper.parseProjectConfigurationFile(outputJsonFile)
    }

    /**
     * provides project configuration for revision to compare. If project configuration file doesn`t exists creates it
     * else parses information from existing
     *
     * @return project configuration
     */
    fun provideRevisionToCompareConfiguration(): ProjectConfiguration {
        val outputJsonFile = createJsonFileNameByRevision(revisionToCompare)
        if (!isProjectConfigurationJsonExists(File(outputJsonFile))) {
            TempProjectCreator(revisionToCompare, TEMP_FOLDER_NAME).createProjectWithRevToCompare()
            ProjectConfigurationCreator(revisionToCompare, tempDirectory).createProjectConfigurationFile()
        }

        return JsonHelper.parseProjectConfigurationFile(outputJsonFile)
    }

    /**
     * provides name for project configuration json file
     *
     * @param revision git revision name
     *
     * @return json project configuration file name
     */
    private fun createJsonFileNameByRevision(revision: String) = "$outputJsonDirectory$revision.json"

    /**
     * checks if project configuration file exists
     *
     * @return true if file exists
     */
    private fun isProjectConfigurationJsonExists(outputJsonFile: File) = outputJsonFile.exists()
}