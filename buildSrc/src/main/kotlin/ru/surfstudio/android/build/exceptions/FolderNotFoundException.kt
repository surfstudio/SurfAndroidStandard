package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Throw when folder doesn't exist somewhere
 */
class FolderNotFoundException(message: String) : GradleException(message)