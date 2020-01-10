package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.GradleException

/**
 * Node not found in git tree
 */
class GitNodeNotFoundException(commit: RevCommit) : GradleException(
        "Node for commit \"${commit.name}\" not found in git tree"
)