package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration
import ru.surfstudio.android.build.tasks.currentDirectory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Provides project configuration
 *
 * @param [currentRevision] revision of project to provide configuration for
 * @param [revisionToCompare] directory of the project to provide configuration for
 */
class ProjectConfigurationProvider(
        private val currentRevision: String,
        private val revisionToCompare: String
) {
    private val outputJsonDirectory = "$currentDirectory/$OUTPUT_JSON_FOLDER_PATH"
    private val tempDirectory = "$currentDirectory/$TEMP_FOLDER_NAME"

    /**
     * provides project configuration for current revision. If project configuration file doesn`t exists creates it
     * else parses information from existing
     *
     * @return project configuration
     */
    fun provideCurrentRevisionConfiguration(): ProjectConfiguration {
        val outputJsonFile = createJsonFileNameByRevision(currentRevision)
        if (!isProjectConfigurationJsonExists(File(outputJsonFile))) {
            runCreateProjectConfigurationTask(createRunForCurrentCommand(currentRevision))
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
            runCreateProjectConfigurationTask(createRunForTempCommand(revisionToCompare))

        }
        return JsonHelper.parseProjectConfigurationFile(outputJsonFile)
    }


    private fun createRunForTempCommand(revisionToCompare: String): String {
        return "./gradlew $GRADLE_TASK_CREATE_FROM_TEMP " +
                " -P$PATH_TO_FILE=$tempDirectory -P$REVISION=$revisionToCompare"
    }

    private fun createRunForCurrentCommand(currentRevision: String): String {
        return "./gradlew $GRADLE_TASK_CREATE_PROJECT_CONFIGURATION " +
                " -P$PATH_TO_FILE=$currentDirectory -P$REVISION=$currentRevision"
    }

    private fun createJsonFileNameByRevision(revisionToCompare: String): String {
        return "$outputJsonDirectory$revisionToCompare.json"
    }

    /**
     * checks if project configuration file exists
     *
     * @return true if file exists
     */
    private fun isProjectConfigurationJsonExists(outputJsonFile: File): Boolean {
        return outputJsonFile.exists()
    }

    /**
     * runs project configuration creating task with gradlew from command line
     */
    private fun runCreateProjectConfigurationTask(commandRunTask: String) {
        Runtime.getRuntime().exec(commandRunTask).waitFor(300, TimeUnit.SECONDS)
    }


}