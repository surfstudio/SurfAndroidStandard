package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.Commit

/**
 * Node not found in git tree
 */
class GitNodeNotFoundException(commit: Commit) : GradleException(
        "Node for commit \"${commit.standardHash}\" not found in git tree"
)