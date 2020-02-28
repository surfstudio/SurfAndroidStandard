package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException

/**
 * Release notes formatting exception
 */
class ReleaseNotesFormatException(message: String) : GradleException(message)