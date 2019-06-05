package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.exceptions.ModuleNotFoundInComponentsJsonException
import ru.surfstudio.android.build.exceptions.SameArtifactVersionInArtifactoryException

/**
 * Check same artifact version in artifactory.
 */
open class CheckSameDependencyArtifactsInArtifactory : DefaultTask() {

    /**
     * Check artifact with same version in artifactory
     *
     * @throws SameArtifactVersionInArtifactoryException when artifact with same
     * version exists in artifactory
     */
    @TaskAction
    fun check() {
        val library = Components.libraries.find { it.name == project.name }
                ?: throw ModuleNotFoundInComponentsJsonException(project.name)

        if (Artifactory.isArtifactExists(library.name, library.projectVersion)) {
            throw SameArtifactVersionInArtifactoryException(library.name, library.projectVersion)
        }
    }
}