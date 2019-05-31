package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.model.ProjectConfiguration
import ru.surfstudio.android.build.tasks.currentDirectory
import java.io.File

private const val OUTPUT_FOLDER_NAME = "outputs"
private const val BUILD_FOLDER_NAME = "build"
private const val CURRENT_TASK_FOLDER_NAME = "check-stable-components-changed-task"

class ProjectConfigurationProvider(
        private val revision: String,
        private val directory: String
) {
    private val outputJsonDir = "$currentDirectory/$BUILD_FOLDER_NAME/$OUTPUT_FOLDER_NAME/$CURRENT_TASK_FOLDER_NAME/"
    private val outputJsonFile = "$outputJsonDir$revision.json"

    fun getProjectConfiguration(): ProjectConfiguration {
        return if (checkProjectConfigurationJsonExists()) {
            JsonHelper.parseProjectConfigurationFile(outputJsonFile)
        } else {
            val newProjectConfiguration = ProjectConfigurationCreator(revision, directory).getProjectConfiguration()
            saveProjectConfiguration(newProjectConfiguration)
            newProjectConfiguration
        }
    }

    private fun saveProjectConfiguration(projectConfiguration: ProjectConfiguration) {
        val fileOutputJson = createFolderAndFilesForOutputResult()
        JsonHelper.writeProjectConfigurationJson(projectConfiguration, fileOutputJson)
    }

    private fun createFolderAndFilesForOutputResult(): File {

        val folderOutputs = File("$currentDirectory/$BUILD_FOLDER_NAME/$OUTPUT_FOLDER_NAME")
        if (!folderOutputs.exists()) folderOutputs.mkdir()
        val folderCurrentTask = File("$currentDirectory/$BUILD_FOLDER_NAME/$OUTPUT_FOLDER_NAME/$outputJsonDir")
        if (!folderCurrentTask.exists()) folderCurrentTask.mkdir()

        val fileOutputJson = File(outputJsonFile)
        if (fileOutputJson.exists()) {
            fileOutputJson.delete()
        }
        fileOutputJson.createNewFile()
        return fileOutputJson
    }

    private fun checkProjectConfigurationJsonExists(): Boolean {
        return File(outputJsonFile).exists()
    }
}