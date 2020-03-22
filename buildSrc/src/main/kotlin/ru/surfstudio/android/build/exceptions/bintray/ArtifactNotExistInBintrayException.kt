package ru.surfstudio.android.build.exceptions.bintray

import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.dependency.AndroidStandardDependency

/**
 * Artifact doesn't exist in bintray exception
 */
class ArtifactNotExistInBintrayException(
        libraryName: String,
        standardDependency: AndroidStandardDependency
) : GradleException(
        "In library : $libraryName. Dependency ${standardDependency.name} with version " +
                "${standardDependency.component.projectVersion} doesn't exist in bintray."
)