package ru.surfstudio.android.build.exceptions.component

import org.gradle.api.GradleException

/**
 * Found stable components which depend on the new one
 */
class DependentComponentsStableException(
    libraryNames: List<String>
) : GradleException("Stable libraries: $libraryNames")