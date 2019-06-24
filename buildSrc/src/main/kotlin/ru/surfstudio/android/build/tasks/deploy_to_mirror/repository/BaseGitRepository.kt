package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit

/**
 * Parent class for git repository
 */
abstract class BaseGitRepository {

    protected abstract val repository: Repository
    protected abstract val repositoryName: String

    protected val git by lazy { Git(repository) }

    /**
     * Delete repository
     */
    open fun delete() = repository.close()

    /**
     * Get [RevCommit] by hash
     */
    fun getCommit(commitHash: String): RevCommit = git.log()
            .add(ObjectId.fromString(commitHash))
            .setMaxCount(1)
            .call()
            .first()

    /**
     * Return branch by commit hash
     */
    fun getBranch(commit: String): Ref = git.branchList()
            .setContains(commit)
            .setListMode(ListBranchCommand.ListMode.ALL)
            .call()
            .first()

    /**
     * Get all branches
     */
    fun getAllBranches(): List<Ref> = git.branchList().call()

    /**
     * Get all commits
     *
     * @param startHash - commit to start build tree
     * @param maxSize - max size of commits
     */
    fun getAllCommits(startHash: String, maxSize: Int): Iterable<RevCommit> {
        return git.log()
                .all()
                .add(ObjectId.fromString(startHash))
                .setMaxCount(maxSize)
                .call()
                .toList()
    }

    fun resetBranch(commit: RevCommit) {
        git.checkout()

        val ref = getBranch(commit.name)
        git.reset()
                .setMode(ResetCommand.ResetType.HARD)
                .setRef(ref.name)
                .call()
    }
}