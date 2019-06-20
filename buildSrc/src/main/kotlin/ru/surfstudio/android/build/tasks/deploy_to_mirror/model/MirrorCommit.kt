package ru.surfstudio.android.build.tasks.deploy_to_mirror.model

import org.eclipse.jgit.revwalk.RevCommit

const val STANDARD_COMMIT_PREFIX = "ANDROID-STANDARD-COMMIT=\""
const val STANDARD_COMMIT_POSTFIX = "\""

/**
 * Commit in mirror repository
 */
data class MirrorCommit(
        private val revCommit: RevCommit
) : Commit(revCommit) {

    override val parents: List<MirrorCommit> = revCommit.parents.map { MirrorCommit(it) }
    override val standardHash: String = revCommit.fullMessage
            .substringAfterLast(STANDARD_COMMIT_PREFIX)
            .substringBefore(STANDARD_COMMIT_POSTFIX)

    val isStandardCommit get() = standardHash.isNotEmpty()
}