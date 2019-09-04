package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException

/**
 * Throw when branch not found
 */
class BranchNotFoundException(branch: String) : GradleException(
        "Can't find branch \"$branch\""
)