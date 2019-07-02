package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.RevCommitNotFoundException
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitType
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitWithBranch
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.mirrorStandardHash
import ru.surfstudio.android.build.utils.standardHash
import ru.surfstudio.android.build.utils.type

private const val HEAD = "HEAD"

/**
 * Class for mirroring android standard to mirror repository
 * Only [filesToMirror] and [foldersToMirror] are mirrored
 */
class MirrorManager(
        private val componentDirectory: String,
        private val standardRepository: StandardRepository,
        private val mirrorRepository: MirrorRepository,
        private val standardDepthLimit: Int,
        private val mirrorDepthLimit: Int
) {

    private val gitTree = GitTree(standardRepository, mirrorRepository)

    private val diffManager = GitDiffManager(
            standardRepository.repositoryPath.path,
            mirrorRepository
    )

    private val filesToMirror = listOf(
            "build.gradle",
            "gradle.properties",
            "settings.gradle"
    )
    private val foldersToMirror = listOf(
            componentDirectory,
            "buildSrc",
            "common"
    )

    /**
     * Mirrors standard repository to mirror repository.
     * Builds git tree presentation and then applies to mirror repository
     *
     * @param rootCommitHash - top commit for mirroring
     */
    fun mirror(rootCommitHash: String) {
        val standardCommits = standardRepository.getAllCommits(rootCommitHash, standardDepthLimit)

        val rootCommit = standardCommits.find { it.name == rootCommitHash }
                ?: throw RevCommitNotFoundException(rootCommitHash)

        val mirrorCommits: Set<RevCommit> = mirrorRepository.getAllBranches()
                .flatMap { mirrorRepository.getAllCommits(it.objectId.name, mirrorDepthLimit) }
                .filter { it.mirrorStandardHash.isNotEmpty() }
                .toSet()

        gitTree.buildGitTree(rootCommit, standardCommits, mirrorCommits)
        applyGitTreeToMirror()
    }

    /**
     * For all git tree commits apply them to mirror repository
     */
    private fun applyGitTreeToMirror() {
        gitTree.commitsToCommit.forEach {
            when (it.type) {
                CommitType.SIMPLE -> commit(it)
                CommitType.MERGE -> merge(it)
                CommitType.MIRROR_START_POINT -> createMirrorStartCommit(it)
            }
        }
    }

    /**
     * creates start commit of git tree in mirror repository
     *
     * @param commit start commit
     */
    private fun createMirrorStartCommit(commit: CommitWithBranch) {
        val mirrorCommit = gitTree.getStartMirrorCommitByStandardHash(commit.commit.standardHash)
        mirrorRepository.reset(mirrorCommit.commit)
        mirrorRepository.checkoutBranch(mirrorCommit.branch)
    }

    /**
     * creates commit in mirror repository by
     * getting all changes for [commit]
     * in standard repository and applying them to mirror repository
     *
     * @param commit commit to apply
     */
    private fun commit(commit: CommitWithBranch) {
        standardRepository.reset(commit.commit)
        checkoutMirrorBranchForCommit(commit)


        val changes = standardRepository.getChanges(commit.commit).filter(::shouldMirror)
        applyChanges(changes)
        val commitHash = mirrorRepository.commit(commit.commit) ?: EMPTY_STRING
        commit.mirrorCommitHash = commitHash
    }

    /**
     * handles checkout right branch for new commit
     *
     * @param commit commit
     */
    private fun checkoutMirrorBranchForCommit(commit: CommitWithBranch) {
        with(mirrorRepository) {
            val parent = gitTree.getParent(commit)
            checkoutBranch(parent.branch)
            if (parent.mirrorCommitHash.isNotEmpty()) {
                checkoutCommit(parent.mirrorCommitHash)
            }
            checkoutBranch(commit.branch)
        }
    }

    /**
     * creates merge commit by getting merge parents for commit
     * and merging them.
     * In case of conflicts just copies file from standard repository to mirror repository
     *
     * @param commit commit to apply
     */
    private fun merge(commit: CommitWithBranch) {
        standardRepository.reset(commit.commit)

        val mainBranch = commit.branch
        val secondBranch = gitTree.getMergeParents(commit)
                .map(CommitWithBranch::branch)
                .first { it != mainBranch }

        mirrorRepository.checkoutBranch(mainBranch)

        val conflicts = mirrorRepository.merge(secondBranch)
        conflicts.forEach {
            val filePath = it.replaceFirst("${mirrorRepository.repositoryPath.path}/", EMPTY_STRING)
            diffManager.modify(filePath)
        }

        val commitHash = mirrorRepository.commit(commit.commit)
        commit.mirrorCommitHash = commitHash ?: EMPTY_STRING
    }

    /**
     * apply changes to mirror repository
     *
     * @param changes list of changes to apply
     */
    private fun applyChanges(changes: List<DiffEntry>) {
        changes.forEach {
            when (it.type) {
                DiffEntry.ChangeType.ADD -> diffManager.add(it)
                DiffEntry.ChangeType.COPY -> diffManager.copy(it)
                DiffEntry.ChangeType.DELETE -> diffManager.delete(it)
                DiffEntry.ChangeType.MODIFY -> diffManager.modify(it)
                DiffEntry.ChangeType.RENAME -> diffManager.rename(it)
            }
        }
    }

    /**
     * check if should apply specified change to mirror repository
     *
     * @param diffEntry change to check
     *
     * @return true if specified change should be included in commit
     */
    private fun shouldMirror(diffEntry: DiffEntry): Boolean {
        val newPath = diffEntry.newPath.substringBeforeLast("/")
        val oldPath = diffEntry.oldPath.substringBeforeLast("/")

        return when (diffEntry.type) {
            DiffEntry.ChangeType.ADD -> checkPathMirroring(newPath)
            DiffEntry.ChangeType.COPY -> checkPathMirroring(newPath) || checkPathMirroring(oldPath)
            DiffEntry.ChangeType.DELETE -> checkPathMirroring(oldPath)
            DiffEntry.ChangeType.MODIFY -> checkPathMirroring(oldPath)
            DiffEntry.ChangeType.RENAME -> checkPathMirroring(newPath) || checkPathMirroring(oldPath)
        }
    }

    /**
     * checks if this path is for specified objects to mirror
     *
     * @param path path to file
     *
     * @return true if changed file is contained in [filesToMirror] or [foldersToMirror]
     */
    private fun checkPathMirroring(path: String): Boolean {
        return filesToMirror.contains(path) || foldersToMirror.any { path.startsWith(it) }
    }
}