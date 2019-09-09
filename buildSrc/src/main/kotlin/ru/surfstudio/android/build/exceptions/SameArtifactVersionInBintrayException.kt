package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Artifact version in build process same as artifact version in bintray
 */
class SameArtifactVersionInBintrayException(
        artifactName:
        String, artifactVersion: String
) : GradleException(
        "Artifact - $artifactName with same version $artifactVersion exists in bintray"
)