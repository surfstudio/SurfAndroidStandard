package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration
import ru.surfstudio.android.build.tasks.currentDirectory
import java.io.File
import java.util.concurrent.TimeUnit

private const val OUTPUT_FOLDER_NAME = "outputs"
private const val BUILD_FOLDER_NAME = "build"
private const val CURRENT_TASK_FOLDER_NAME = "check-stable-components-changed-task"
private const val GRADLE_TASK_CREATE_PROJECT_CONFIGURATION = "CreateProjectConfiguration"

/**
 * Provides project configuration
 *
 * @param revision revision of project to provide configuration for
 * @param directory directory of the project to provide configuration for
 */
class ProjectConfigurationProvider(
        revision: String,
        directory: String
) {
    private val outputJsonDir = "$currentDirectory/$BUILD_FOLDER_NAME/$OUTPUT_FOLDER_NAME/$CURRENT_TASK_FOLDER_NAME/"
    private val outputJsonFile = "$outputJsonDir$revision.json"
    private val commandRunTaskCreate = "$directory/gradlew $GRADLE_TASK_CREATE_PROJECT_CONFIGURATION" +
            " -PpathToFile=$directory -Prevision=$revision"

    /**
     * provides project configuration. If project configuration file doesn`t exists creates it
     * else parses information from existing
     *
     * @return project configuration
     */
    fun provideProjectConfiguration(): ProjectConfiguration {
        if (!checkProjectConfigurationJsonExists()) {
            runCreateProjectConfigurationTask()
        }
        return JsonHelper.parseProjectConfigurationFile(outputJsonFile)
    }

    /**
     * checks if project configuration file exists
     *
     * @return true if file exists
     */
    private fun checkProjectConfigurationJsonExists(): Boolean {
        return File(outputJsonFile).exists()
    }

    /**
     * runs project configuration creating task with gradlew from command line
     */
    private fun runCreateProjectConfigurationTask() {
        Runtime.getRuntime().exec(commandRunTaskCreate).waitFor(300, TimeUnit.SECONDS)
    }
}