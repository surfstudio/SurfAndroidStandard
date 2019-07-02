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
 * Git manager
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
    private val folderToMirror = listOf(
            componentDirectory,
            "buildSrc",
            "common"
    )

    /**
     * Mirror standard repository and mirror repository
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
        start()
    }

    private fun start() {
        gitTree.commitsToCommit.forEach {
            when (it.type) {
                CommitType.SIMPLE -> commit(it)
                CommitType.MERGE -> merge(it)
                CommitType.MIRROR_START_POINT -> mirrorStartPoint(it)
            }
        }
    }

    private fun commit(commit: CommitWithBranch) {
        val changes = standardRepository.getChanges(commit.commit).filter(::shouldMirror)
        val parent = gitTree.getParent(commit)

        standardRepository.reset(commit.commit)

        with(mirrorRepository) {
            checkoutBranch(parent.branch)
            if (parent.mirrorCommitHash.isNotEmpty()) {
                checkoutCommit(parent.mirrorCommitHash)
            }
            checkoutBranch(commit.branch)
        }
        applyChanges(changes)
        val commitHash = mirrorRepository.commit(commit.commit) ?: EMPTY_STRING
        commit.mirrorCommitHash = commitHash
    }

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

    private fun mirrorStartPoint(commit: CommitWithBranch) {
        val mirrorCommit = gitTree.getStartMirrorCommitByStandardHash(commit.commit.standardHash)
        mirrorRepository.reset(mirrorCommit.commit)
        mirrorRepository.checkoutBranch(mirrorCommit.branch)
    }


    //    private fun commitChanges() {
//        val standardStartCommit = gitTree.getStandardStartCommit()
//        val mirrorStartCommit = gitTree.getMirrorCommitByStandard(standardStartCommit.standardHash)
//        val commits = gitTree.getCommitsWithChanges()
//
//        if (commits.isEmpty()) return
//
//        standardRepository.reset(standardStartCommit)
//        standardRepository.checkout(standardStartCommit)
//
//        mirrorRepository.reset(mirrorStartCommit)
//        mirrorRepository.checkout(mirrorStartCommit)
//
//        commits.forEach { handleCommit(it) }
//    }
//
//    private fun handleCommit(commit: RevCommit) {
//        standardRepository.reset(commit)
//        standardRepository.checkout(commit)
//
//        if (commit.isMergeCommit) {
//            merge(commit)
//        } else {
//            val diff = standardRepository.getChanges(commit)
//                    .filter(::shouldMirror)
//            diff.forEach {
//                when (it.type) {
//                    ADD -> diffManager.add(it)
//                    COPY -> diffManager.copy(it)
//                    DELETE -> diffManager.delete(it)
//                    MODIFY -> diffManager.modify(it)
//                    RENAME -> diffManager.rename(it)
//                }
//            }
//            commit(commit)
//        }
//    }
//
//    private fun merge(commit: RevCommit) {
//        //TODO
//    }
//
//    private fun commit(commit: RevCommit) {
//        mirrorRepository.commit(commit)
//    }
//
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

    private fun checkPathMirroring(path: String): Boolean {
        return filesToMirror.contains(path) || folderToMirror.any { path.startsWith(it) }
    }
}