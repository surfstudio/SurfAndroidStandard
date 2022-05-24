package ru.surfstudio.android.build.exceptions.branch

import org.gradle.api.GradleException

/**
 * Throw when branch not found
 */
class BranchNotFoundException(branch: String) : GradleException(
        "Can't find branch \"$branch\""
)