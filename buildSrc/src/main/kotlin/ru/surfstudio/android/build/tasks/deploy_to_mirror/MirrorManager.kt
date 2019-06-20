package ru.surfstudio.android.build.tasks.deploy_to_mirror

import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.MirrorCommit
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.StandardCommit
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.data_structure.GitTree
import ru.surfstudio.android.build.utils.getParents

/**
 * Git manager
 */
class MirrorManager(
        private val standardRepository: StandardRepository,
        private val mirrorRepository: MirrorRepository,
        private val standardDepthLimit: Int,
        private val mirrorDepthLimit: Int
) {

    /**
     * Mirror standard repository and mirror repository
     *
     * @param rootCommit - top commit for mirroring
     */
    fun mirror(rootCommit: StandardCommit) {
        val ends: Set<MirrorCommit> = getEnds()
        val gitTree = buildGitTree(rootCommit, ends)

        gitTree.print()
    }

    /**
     * Get mirror end commits
     */
    private fun getEnds(): Set<MirrorCommit> {
        val result = mutableSetOf<MirrorCommit>()

        val topBranchCommits = mirrorRepository.getAllBranches()
                .map { mirrorRepository.getCommit(it.objectId.name) }
                .filter(MirrorCommit::isStandardCommit)
        result.addAll(topBranchCommits)

        if (mirrorDepthLimit > 1) {
            val branchParentCommits = topBranchCommits
                    .flatMap { it.getParents(mirrorDepthLimit - 1) }
                    .toSet()
                    .filter(MirrorCommit::isStandardCommit)

            result.addAll(branchParentCommits)
        }

        return result
    }

    /**
     * Build GitTree with correct structure
     *
     * @param rootCommit - top commit
     * @param endCommits - end commits
     */
    private fun buildGitTree(rootCommit: StandardCommit, endCommits: Set<MirrorCommit>): GitTree {
        val gitTree = GitTree()
//        gitTree.setRoot(rootCommit)
//        gitTree.setEnds(endCommits)
//
//        val iCommits = mutableSetOf<RevCommit>().apply {
//            add(rootCommit)
//        }
//        val iParents = mutableSetOf<RevCommit>()
//
//        for (i in 1..standardDepthLimit) {
//            iCommits.forEach { commit ->
//                val parents = commit.parents.toList()
//                gitTree.add(commit, parents)
//
//                iParents.addAll(parents.filter { !endCommits.contains(it) })
//            }
//
//            iCommits.clear()
//            iCommits.addAll(iParents)
//        }

        return gitTree
    }
}