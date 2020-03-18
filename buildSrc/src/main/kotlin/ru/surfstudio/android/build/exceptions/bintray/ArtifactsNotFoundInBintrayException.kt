package ru.surfstudio.android.build.exceptions.bintray

import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.module.Library

/**
 * Artifacts don't exist in bintray
 */
class ArtifactsNotFoundInBintrayException(
        libraries: List<Library>
) : GradleException(
        "Artifacts: ${libraries.map { "${it.artifactName}:${it.projectVersion}" }} don't exist in bintray."
)