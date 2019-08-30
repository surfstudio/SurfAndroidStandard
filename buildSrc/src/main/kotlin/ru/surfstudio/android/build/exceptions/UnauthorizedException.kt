package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Unauthorized user
 */
class UnauthorizedException(message: String) : GradleException(message)