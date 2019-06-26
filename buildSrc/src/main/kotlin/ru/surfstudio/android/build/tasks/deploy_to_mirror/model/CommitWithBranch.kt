package ru.surfstudio.android.build.tasks.deploy_to_mirror.model

import org.eclipse.jgit.revwalk.RevCommit

data class CommitWithBranch(
        val commit: RevCommit,
        val branch: String
)