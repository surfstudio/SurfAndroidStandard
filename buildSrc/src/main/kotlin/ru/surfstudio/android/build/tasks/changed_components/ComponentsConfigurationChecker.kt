package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.changed_components.models.ComponentCheckResult
import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration

/**
 * Class for checking if components configuration has changed
 * Configuration includes: info from components.json file, info about dependencies versions from config.gradle file
 * and also values from config.gradle: libMinSdkVersion, targetSdkVersion,moduleVersionCode, compileSdkVersion
 *
 * @param currentRevision current revision of project to check
 * @param revisionToCompare revision of this project to compare with current
 */
class ComponentsConfigurationChecker(
        private val currentRevision: String,
        private val revisionToCompare: String
) {

    /**
     * gets information about
     *
     * @return result of comparision for each component
     */
    fun getChangeInformationForComponents(): List<ComponentCheckResult> {
        val projectConfigurationProvider = ProjectConfigurationProvider(currentRevision, revisionToCompare)
        val currentRevisionConfiguration = projectConfigurationProvider.provideCurrentRevisionConfiguration()
        val revisionToCompareConfiguration = projectConfigurationProvider.provideRevisionToCompareConfiguration()
        println("start compareProjects")
        return compareProjects(currentRevisionConfiguration, revisionToCompareConfiguration)
    }

    /**
     * compares component configurations for change
     *
     * @return result of changed components
     */
    private fun compareProjects(
            currentRevisionConfiguration: ProjectConfiguration,
            revisionToCompareConfiguration: ProjectConfiguration
    ): List<ComponentCheckResult> {
        return ProjectConfigurationComparator(currentRevisionConfiguration, revisionToCompareConfiguration).compareProjectConfigurations()
    }
}