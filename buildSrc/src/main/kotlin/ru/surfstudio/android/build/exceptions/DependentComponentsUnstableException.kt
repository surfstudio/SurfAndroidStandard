package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Component unstable
 */
class DependentComponentsUnstableException(
        libraryNames: List<String>
) : GradleException("Unstable libraries: $libraryNames")