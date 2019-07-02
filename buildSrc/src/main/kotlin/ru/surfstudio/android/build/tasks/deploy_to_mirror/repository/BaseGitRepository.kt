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

    fun reset(revCommit: RevCommit) {
        git.reset()
                .setMode(ResetCommand.ResetType.HARD)
                .setRef(revCommit.name)
                .call()
    }

    fun checkoutBranch(branchName: String) {
        val isBranchCreated = git.branchList().call()
                .map { it.name }
                .extractBranchNames()
                .contains(branchName)

        git.checkout()
                .setCreateBranch(!isBranchCreated)
                .setName(branchName)
                .call()
    }

    fun checkoutCommit(commitHash: String) {
        git.checkout()
                .setName(commitHash)
                .call()
    }

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

    private fun getBranchName(commit: String): String {
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

    fun getBranchById(id: String): Ref? = git.branchList()
            .setListMode(ListBranchCommand.ListMode.ALL)
            .call()
            .filter { it.name != "HEAD" }
            .find { it.objectId.name == id }

    fun getBranchesByContainsId(id: String): List<Ref> = git.branchList()
            .setListMode(ListBranchCommand.ListMode.ALL)
            .setContains(id)
            .call()

    fun merge(secondBranch: String) = git.merge()
            .setCommit(false)
            .include(getBranch(secondBranch))
            .call()
            ?.conflicts
            ?.map { it.key } ?: emptyList()


    fun getBranch(refName: String): Ref {
        return git.branchList().call()
                .find { it.name.substringAfterLast("/") == refName }
                ?: throw BranchNotFoundException(refName)
    }

    fun addToIndex(filePath: String) = git.add()
            .addFilepattern(filePath)
            .call()

}