package ru.surfstudio.android.build.tasks.artifactory_tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check artifact for android standard dependencies exist in artifactory
 */
open class CheckExistingDependencyArtifactsInArtifactoryTask : DefaultTask() {

    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()

        Artifactory.checkLibrariesStandardDependenciesExisting(component)
    }
}