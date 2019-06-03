package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult
import ru.surfstudio.android.build.tasks.currentDirectory

private const val TEMP_FOLDER_NAME = "temp"

/**
 * Class for checking if components configuration has changed
 * Configuration includes: info from components.json file, info about dependencies versions from config.gradle file
 * and also values from config.gradle: libMinSdkVersion, targetSdkVersion,moduleVersionCode, compileSdkVersion
 *
 * @param currentRevision current revision of project to check
 * @param revisionToCompare revision of this project to compare with current
 */
class ComponentsConfigChecker(
        private val currentRevision: String,
        private val revisionToCompare: String
) {
    private val tempDirectory = "$currentDirectory/$TEMP_FOLDER_NAME"
    /**
     * Creates second project for compare with [revisionToCompare]
     * Creates project configurations for both revisions
     * and compares them finding which components configurations changed
     *
     * @return result of comparision for each component
     */
    fun getChangeInformationForComponents(): List<ComponentCheckResult> {
        createProjectWithRevisionToCompare()
        return compareProjects()
    }

    /**
     * create second project with copy current project an checkout [revisionToCompare] on it
     */
    private fun createProjectWithRevisionToCompare() {
        SecondProjectCreator(revisionToCompare, TEMP_FOLDER_NAME).createProjectWithRevToCompare()
    }

    /**
     * compares component configurations for change
     *
     * @return result of changed components
     */
    private fun compareProjects(): List<ComponentCheckResult> {
        val currentInfo = ProjectConfigurationProvider(currentRevision, currentDirectory).provideProjectConfiguration()
        val infoToCompare = ProjectConfigurationProvider(revisionToCompare, tempDirectory).provideProjectConfiguration()
        return ProjectConfigurationComparator(currentInfo, infoToCompare).compareProjectInfos()
    }
}