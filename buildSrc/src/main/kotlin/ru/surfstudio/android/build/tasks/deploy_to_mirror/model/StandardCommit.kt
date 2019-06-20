package ru.surfstudio.android.build.tasks.deploy_to_mirror.model

import org.eclipse.jgit.revwalk.RevCommit

/**
 * Commit in standard repository
 */
data class StandardCommit(
        private val revCommit: RevCommit
) : Commit(revCommit) {

    override val standardHash: String = revCommit.name
    override val parents: List<StandardCommit> = revCommit.parents.map { StandardCommit(it) }
}