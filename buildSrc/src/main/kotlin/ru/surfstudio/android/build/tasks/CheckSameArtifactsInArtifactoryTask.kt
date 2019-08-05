package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.exceptions.SameArtifactVersionInArtifactoryException
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check same artifact version in artifactory.
 */
open class CheckSameArtifactsInArtifactoryTask : DefaultTask() {

    /**
     * Check artifact with same version in artifactory
     *
     * @throws SameArtifactVersionInArtifactoryException when artifact with same
     * version exists in artifactory
     */
    @TaskAction
    fun check() {
        val allowSameVersion = project.property(GradleProperties.DEPLOY_SAME_VERSION_ARTIFACTORY) as? Boolean
                ?: false

        if (allowSameVersion) return

        val component = project.getPropertyComponent()

        component.libraries.forEach { library ->
            if (Artifactory.isArtifactExists(library.name, library.projectVersion)) {
                throw SameArtifactVersionInArtifactoryException(library.name, library.projectVersion)
            }
        }

    }
}