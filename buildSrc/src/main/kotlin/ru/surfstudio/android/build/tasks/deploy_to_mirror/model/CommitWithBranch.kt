package ru.surfstudio.android.build.tasks.deploy_to_mirror.model

import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.utils.EMPTY_STRING

data class CommitWithBranch(
        val commit: RevCommit,
        var branch: String = EMPTY_STRING,
        var type: CommitType = CommitType.SIMPLE
) {

    override fun toString() = "$type $branch ${commit.shortMessage}"
}

enum class CommitType {
    SIMPLE, MERGE, MIRROR_START_POINT
}