package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Component stable
 */
class DependentComponentsStableException(
        libraryNames: List<String>
) : GradleException("Stable libraries: $libraryNames")