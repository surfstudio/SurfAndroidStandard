package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.Folders.OUTPUT_JSON_FOLDER_PATH
import ru.surfstudio.android.build.Folders.TEMP_FOLDER_NAME
import ru.surfstudio.android.build.GradleProperties.CREATE_PROJECT_CONFIGURATION_PATH_TO_PROJECT
import ru.surfstudio.android.build.GradleProperties.CREATE_PROJECT_CONFIGURATION_REVISION
import ru.surfstudio.android.build.GradleTasksNames.GRADLE_TASK_CREATE_FROM_TEMP
import ru.surfstudio.android.build.GradleTasksNames.GRADLE_TASK_CREATE_PROJECT_CONFIGURATION
import ru.surfstudio.android.build.tasks.changed_components.CommandLineRunner.runCommandWithResult
import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration
import ru.surfstudio.android.build.tasks.currentDirectory
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
            runCommandWithResult(createCommandForCurrentRevision(currentRevision), File(currentDirectory))
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
            runCommandWithResult(createCommandForRevisionToCompare(revisionToCompare), File(currentDirectory))

        }

        return JsonHelper.parseProjectConfigurationFile(outputJsonFile)
    }

    /**
     * provides command running task creating project configuration file for revision to compare
     *
     * @return command to run [GRADLE_TASK_CREATE_FROM_TEMP] task with parameters
     */
    private fun createCommandForRevisionToCompare(revisionToCompare: String): String {
        return "./gradlew $GRADLE_TASK_CREATE_FROM_TEMP " +
                "-P$CREATE_PROJECT_CONFIGURATION_PATH_TO_PROJECT=$tempDirectory " +
                "-P$CREATE_PROJECT_CONFIGURATION_REVISION=$revisionToCompare"
    }

    /**
     * provides command running task creating project configuration file for current revision
     *
     * @param currentRevision git current project revision
     *
     * @return command to run [GRADLE_TASK_CREATE_PROJECT_CONFIGURATION] task with parameters
     */
    private fun createCommandForCurrentRevision(currentRevision: String): String {
        return "./gradlew $GRADLE_TASK_CREATE_PROJECT_CONFIGURATION " +
                "-P$CREATE_PROJECT_CONFIGURATION_PATH_TO_PROJECT=$currentDirectory/ " +
                "-P$CREATE_PROJECT_CONFIGURATION_REVISION=$currentRevision"
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