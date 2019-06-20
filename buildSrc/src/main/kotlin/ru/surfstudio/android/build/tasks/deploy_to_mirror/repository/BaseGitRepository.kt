package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.Commit

/**
 * Parent class for git repository
 */
abstract class BaseGitRepository {

    protected abstract val repository: Repository
    protected abstract val repositoryName: String

    private val git by lazy { Git(repository) }

    /**
     * Delete repository
     */
    open fun delete() {
        repository.close()
    }

    /**
     * Get [RevCommit] by hash
     */
    protected fun getRevCommit(commitHash: String): RevCommit = git.log()
            .add(ObjectId.fromString(commitHash))
            .setMaxCount(1)
            .call()
            .first()

    /**
     * Get [Commit]
     */
    abstract fun getCommit(commitHash: String): Commit

    /**
     * Get all branches
     */
    fun getAllBranches(): List<Ref> = git.branchList().call()
}