package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffEntry.ChangeType.*
import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.RevCommitNotFoundException
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.isMergeCommit
import ru.surfstudio.android.build.utils.standardHash
import ru.surfstudio.android.build.utils.type

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

    private val diffManager = GitDiffManager(
            standardRepository.repositoryPath.path,
            mirrorRepository.repositoryPath.path
    )

    private val filesToMirror = listOf(
            "${StandardRepository.TEMP_DIR_PATH}/build.gradle",
            "${StandardRepository.TEMP_DIR_PATH}/gradle.properties",
            "${StandardRepository.TEMP_DIR_PATH}/settings.gradle"
    )
    private val folderToMirror = listOf(
            "${StandardRepository.TEMP_DIR_PATH}/$componentDirectory/",
            "${StandardRepository.TEMP_DIR_PATH}/buildSrc/",
            "${StandardRepository.TEMP_DIR_PATH}/common/"
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
                .filter { it.standardHash.isNotEmpty() }
                .toSet()

        val gitTree = buildGitTree(rootCommit, standardCommits, mirrorCommits)

        gitTree.cut()
//
//sadasd
//
//        commitChanges(gitTree)
    }

    /**
     * Build GitTree with correct structure
     *
     * @param rootCommit - top commit
     * @param endCommits - end commits
     */
    private fun buildGitTree(
            rootCommit: RevCommit,
            standardCommits: Iterable<RevCommit>,
            mirrorCommits: Iterable<RevCommit>
    ): GitTree {
        val gitTree = GitTree()
        gitTree.setRoot(rootCommit)
        gitTree.setEnds(mirrorCommits)

        val iCommits = mutableSetOf(rootCommit)
        val iParents = mutableSetOf<RevCommit>()

        for (standardCommit in standardCommits) {
            iCommits.forEach { commit ->
                val parentHashes = commit.parents.map { it.name }
                val parents = standardCommits.filter { parentHashes.contains(it.name) }

                gitTree.add(commit, parents)

                iParents.addAll(parents.filter { !mirrorCommits.map { it.standardHash }.contains(it.name) })
            }

            iCommits.clear()
            iCommits.addAll(iParents)
            iParents.clear()

            if (iCommits.isEmpty()) break
        }

        return gitTree
    }

    private fun commitChanges(gitTree: GitTree) {
        val standardStartCommit = gitTree.getStandardStartCommit()
        val mirrorStartCommit = gitTree.getMirrorCommitByStandard(standardStartCommit.name)
        val commits = gitTree.getCommitsWithChanges()

        if (commits.isEmpty()) return

        standardRepository.reset(standardStartCommit)
        standardRepository.checkout(standardStartCommit)

        mirrorRepository.reset(mirrorStartCommit)
        mirrorRepository.checkout(mirrorStartCommit)

        commits.forEach { handleCommit(it, gitTree) }
    }

    private fun handleCommit(commit: RevCommit, gitTree: GitTree) {
        standardRepository.reset(commit)
        standardRepository.checkout(commit)

        if (commit.isMergeCommit) {
            merge(commit)
        } else {
            val diff = standardRepository.getChanges(commit)
                    .filter(::shouldMirror)
            diff.forEach {
                when (it.type) {
                    ADD -> diffManager.add(it)
                    COPY -> diffManager.copy(it)
                    DELETE -> diffManager.delete(it)
                    MODIFY -> diffManager.modify(it)
                    RENAME -> diffManager.rename(it)
                }
            }
            commit(commit)
        }
    }

    private fun merge(commit: RevCommit) {
        //TODO
    }

    private fun commit(commit: RevCommit) {
        mirrorRepository.commit(commit)
    }

    private fun shouldMirror(diffEntry: DiffEntry): Boolean {
        val newPath = diffEntry.newPath.substringBeforeLast("/")
        val oldPath = diffEntry.oldPath.substringBeforeLast("/")

        return when (diffEntry.type) {
            ADD -> checkPathMirroring(newPath)
            COPY -> checkPathMirroring(newPath) || checkPathMirroring(oldPath)
            DELETE -> checkPathMirroring(oldPath)
            MODIFY -> checkPathMirroring(oldPath)
            RENAME -> checkPathMirroring(newPath) || checkPathMirroring(oldPath)
        }
    }

    private fun checkPathMirroring(path: String): Boolean {
        return filesToMirror.contains(path) || folderToMirror.any { path.startsWith(it) }
    }
}