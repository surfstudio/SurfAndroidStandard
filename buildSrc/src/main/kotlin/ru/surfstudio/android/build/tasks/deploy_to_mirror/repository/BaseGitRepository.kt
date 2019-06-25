package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.BranchCanNotBeDefinedException
import java.io.File

private const val BRANCH_REF_PATTERN = ".*~\\d+$"

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

    fun checkout(commit: RevCommit) {
        git.checkout()
                .setName(commit.name)
                .call()
    }

    fun getBranchName(commit: String): String {
        val branches = git.branchList()
                .setContains(commit)
                .call()
                .map { it.name.substringAfterLast("/") }
                .toMutableList()

        if (branches.size == 1) return branches[0]

        val refs = git.nameRev()
                .add(ObjectId.fromString(commit))
                .call()
                .values
                .map { it.matches(BRANCH_REF_PATTERN.toRegex()) to it.substringBeforeLast("~") }

        if (refs.isEmpty()) {
            throw BranchCanNotBeDefinedException(
                    commit,
                    branches,
                    refs.map(Pair<Boolean, String>::second)
            )
        }

        refs.forEach {
            if (it.first) {
                branches.remove(it.second)
            } else {
                return it.second
            }
        }

        if (branches.isEmpty() || branches.size > 1) {
            throw BranchCanNotBeDefinedException(
                    commit,
                    branches,
                    refs.map(Pair<Boolean, String>::second)
            )
        }

        return branches[0]
    }
}