package ru.surfstudio.android.build.exceptions.artifactory

import org.gradle.api.GradleException

/**
 * Artifact version in build process same as artifact version in artifactory
 */
class SameArtifactVersionInArtifactoryException(
        artifactName:
        String, artifactVersion: String
) : GradleException(
        "Artifact - $artifactName with same version $artifactVersion exists in artifactory"
)