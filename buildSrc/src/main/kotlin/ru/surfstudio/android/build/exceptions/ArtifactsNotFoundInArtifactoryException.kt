package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.module.Library

/**
 * Artifacts don't exist in artifactory
 */
class ArtifactsNotFoundInArtifactoryException(
        libraries: List<Library>
) : GradleException(
        "Artifacts: ${libraries.map { "${it.artifactName}:${it.projectVersion}" }} don't exist in artifactory."
)