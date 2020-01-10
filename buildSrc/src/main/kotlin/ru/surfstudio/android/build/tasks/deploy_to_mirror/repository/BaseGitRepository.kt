package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.BranchCanNotBeDefinedException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.BranchNotFoundException
import ru.surfstudio.android.build.utils.extractBranchNames
import java.io.File

private const val BRANCH_REF_PATTERN = ".*~\\d+$"

/**
 * Parent class for git repository
 */
abstract class BaseGitRepository {

    abstract val repositoryPath: File
    abstract val repositoryName: String

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
    fun getAllBranches(): List<Ref> = git.branchList()
            .setListMode(ListBranchCommand.ListMode.ALL)
            .call()

    /**
     * Get all commits
     *
     * @param startHash - commit to start build tree
     * @param maxDepth - max size of commits
     */
    fun getAllCommits(startHash: String, maxDepth: Int): Iterable<RevCommit> {
        val result = mutableSetOf<RevCommit>()

        val list = mutableSetOf<RevCommit>().apply {
            add(getCommit(startHash))
        }

        result.addAll(list)

        for (i in 2..maxDepth) {
            val parents = list.flatMap { it.parents.map { getCommit(it.name) } }

            if (parents.isEmpty()) return result

            list.clear()
            list.addAll(parents)
            result.addAll(list)
        }
        return result
    }

    /**
     * get all changes of commit
     *
     * @param commit commit to get changes for
     */
    fun getChanges(commit: RevCommit): MutableList<DiffEntry> {
        val reader = git.repository.newObjectReader()

        val newTreeParser = CanonicalTreeParser().apply {
            reset(reader, commit.tree)
        }

        val oldTreeParser = CanonicalTreeParser().apply {
            reset(reader, commit.parents[0].tree)
        }

        return git.diff()
                .setNewTree(newTreeParser)
                .setOldTree(oldTreeParser)
                .call()
    }

    /**
     * Get branches by id
     *
     * @param id  of object branch should contain
     * @return list of branches
     */
    fun getBranchesByContainsId(id: String): List<Ref> = git.branchList()
            .setListMode(ListBranchCommand.ListMode.ALL)
            .setContains(id)
            .call()

    /**
     * get branch by commit
     *
     * @param commit hash of commit
     * @return branch name
     */
    fun getBranchNameByCommit(commit: String): String {
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

    /**
     * reset repository to commit
     *
     * @param revCommit commit to reset to
     */
    fun reset(revCommit: RevCommit) {
        git.reset()
                .setMode(ResetCommand.ResetType.HARD)
                .setRef(revCommit.name)
                .call()
    }

    /**
     * checkout branch by name. If not exist create
     *
     * @param branchName branch name for checkout
     */
    fun checkoutBranch(branchName: String) {
        git.checkout()
                .setCreateBranch(!isBranchExists(branchName))
                .setName(branchName)
                .call()
    }

    /**
     *
     */
    fun isBranchExists(branchName: String): Boolean {
        return git.branchList().call()
                .map { it.name }
                .extractBranchNames()
                .contains(branchName)
    }

    /**
     * checkout commit
     *
     * @param commitHash commit hash to checkout
     */

    fun checkoutCommit(commitHash: String) {
        git.checkout()
                .setName(commitHash)
                .call()
    }

    /**
     * merge to current branch another one
     *
     * @param secondBranch branch to merge
     *
     * @return conflicts if there are any
     */
    fun merge(secondBranch: String) = git.merge()
            .setCommit(false)
            .include(getBranch(secondBranch))
            .call()
            ?.conflicts
            ?.map { it.key } ?: emptyList()

    /**
     * add to git index
     *
     * @param filePath path to file to add
     */
    fun addToIndex(filePath: String) = git.add()
            .addFilepattern(filePath)
            .call()

    /**
     * create branch
     */
    fun createBranch(branchName: String, startCommit: String) {
        git.branchCreate().setForce(true).setName(branchName).setStartPoint(startCommit).call()
    }

    /**
     * delete branch
     */
    fun deleteBranch(branchName: String) {
        git.branchDelete().setBranchNames(branchName).setForce(true).call()
    }

    /**
     * Get diff from commit to current
     */
    fun diff(commit: String): List<String> {
        val currentCommit = git.repository.resolve("HEAD^{tree}")
        val oldCommit = git.repository.resolve("$commit^{tree}")

        val reader = git.repository.newObjectReader()

        val currentTree = CanonicalTreeParser().apply {
            reset(reader, currentCommit)
        }

        val oldTree = CanonicalTreeParser().apply {
            reset(reader, oldCommit)
        }

        return git.diff()
                .setNewTree(currentTree)
                .setOldTree(oldTree)
                .call()
                .map { entry ->
                    entry.oldPath?.takeIf { it != DiffEntry.DEV_NULL } ?: entry.newPath
                }
    }

    /**
     * get branch by name
     *
     * @param refName name of branch
     */
    private fun getBranch(refName: String): Ref {
        return git.branchList().call()
                .find { it.name.substringAfterLast("/") == refName }
                ?: throw BranchNotFoundException(refName)
    }
}