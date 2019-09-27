package ru.surfstudio.android.build.tasks.deploy_to_mirror.model

import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Commit for mirror repository
 */
data class CommitWithBranch(
        val commit: RevCommit,
        val tags: List<String> = emptyList(),
        var branch: String = EMPTY_STRING,
        var type: CommitType = CommitType.SIMPLE,
        var mirrorCommitHash: String = EMPTY_STRING
) {

    override fun toString() = "$type $branch ${commit.shortMessage}"
}

/**
 * State for commit: simple, merge commit and beginning of mirroring
 */
enum class CommitType {
    SIMPLE, MERGE, MIRROR_START_POINT, COMMITED
}