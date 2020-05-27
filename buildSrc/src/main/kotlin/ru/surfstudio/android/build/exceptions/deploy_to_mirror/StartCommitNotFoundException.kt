package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException

/**
 * Throw when git tree can't find start commit in standard repository
 */
class StartCommitNotFoundException : GradleException(
        "Can't find start commit for mirroring"
)