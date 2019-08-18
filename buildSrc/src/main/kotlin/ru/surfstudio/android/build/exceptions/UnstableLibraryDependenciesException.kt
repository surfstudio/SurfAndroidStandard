package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Unstable libraries
 */
class UnstableLibraryDependenciesException(libDescription: String) : GradleException(libDescription)