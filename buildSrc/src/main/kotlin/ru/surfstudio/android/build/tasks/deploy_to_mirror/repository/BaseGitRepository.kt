package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File


/**
 * Parent class for git repository
 */
abstract class BaseGitRepository {

    protected abstract val repositoryPath: File
    protected abstract val repositoryName: String

    protected val git: Git by lazy { Git.open(repositoryPath) }

    /**
     * Delete repository
     */
    open fun delete() = git.close()

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
    fun getBranchName(commit: String): String {
        val branches = git.branchList()
                .setContains(commit)
                .call()

        return EMPTY_STRING
    }


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

    fun reset(commit: String) {
        git.reset()
                .setMode(ResetCommand.ResetType.HARD)
                .setRef(commit)
                .call()
    }

    fun checkout(commit: RevCommit, branchName: String) {
        git.checkout()
                .setCreateBranch(true)
                .setStartPoint(commit)
                .setName(branchName)
                .call()
    }
}