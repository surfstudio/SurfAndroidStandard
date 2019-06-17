package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.api.Git
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.BranchWithCommitNotFoundException
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Parent class for git repository
 */
abstract class BaseGitRepository {

    protected abstract val git: Git
    protected abstract val repositoryName: String

    /**
     * Delete repository
     */
    abstract fun delete()

    /**
     * Get last commit in branch
     * If branch not found return empty string
     *
     * @param branchName - commit's branch name
     */
    fun getLastCommit(branchName: String): String {
        val branch = git.branchList().call().find { it.name.contains(branchName) }
                ?: return EMPTY_STRING
        return EMPTY_STRING
    }

    /**
     * Return branch name by commit
     * Branch name can't contain "/" symbol
     *
     * @param commit - any branch commit
     */
    fun getBranchByCommit(commit: String): String {
        val branches = git.branchList()
                .setContains(commit)
                .call()
        if (branches.isEmpty()) throw BranchWithCommitNotFoundException(repositoryName, commit)

        return branches.first()
                .name
                .substringAfterLast("/")
    }

//    /**
//     * Create branch
//     */
//    fun createBranch(branchName: String): Ref = git.branchCreate().setName(branchName).call()
}