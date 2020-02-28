package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException

/**
 * When you cut cutTree it must has at least one end
 */
class NoEndsDefineException : GradleException(
        "For cut operation GitTree must has at least one \"end\""
)