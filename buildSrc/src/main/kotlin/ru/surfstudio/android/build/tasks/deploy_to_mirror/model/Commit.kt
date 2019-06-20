package ru.surfstudio.android.build.tasks.deploy_to_mirror.model

import org.eclipse.jgit.revwalk.RevCommit

/**
 * Base class for Commit
 */
abstract class Commit(revCommit: RevCommit) {

    abstract val parents: List<Commit>
    abstract val standardHash: String
}