package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.*
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitWithBranch
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.standardHash

private const val HEAD = "HEAD"

/**
 * Data structure based on tree
 * Use it to build tree, set root and ends elements and delete other
 */
class GitTree(private val standardRepository: StandardRepository) {

    private lateinit var rootNode: Node
    private lateinit var rootCommitWithBranch: CommitWithBranch
    private val mirrorCommitNodes: MutableSet<Node> = mutableSetOf()
    private val nodes: MutableSet<Node> = mutableSetOf()
    private val commitsWithBranch: MutableSet<CommitWithBranch> = mutableSetOf()

    /**
     * Build GitTree with correct structure
     */
    fun buildGitTree(
            rootRevCommit: RevCommit,
            standardRevCommits: Iterable<RevCommit>,
            mirrorRevCommits: Iterable<RevCommit>
    ) {
        setRoot(rootRevCommit)
        setMirrorCommits(mirrorRevCommits)

        val iCommits = mutableSetOf(rootRevCommit)
        val iParents = mutableSetOf<RevCommit>()

        for (standardCommit in standardRevCommits) {
            iCommits.forEach { commit ->
                val parentHashes = commit.parents.map { it.name }
                val parents = standardRevCommits.filter { parentHashes.contains(it.name) }

                add(commit, parents)

                iParents.addAll(parents.filter { !mirrorRevCommits.map { it.standardHash }.contains(it.name) })
            }

            iCommits.clear()
            iCommits.addAll(iParents)
            iParents.clear()

            if (iCommits.isEmpty()) break
        }

        cut()
        configureBranches()

        commitsWithBranch
                .sortedBy { it.commit.commitTime }
                .forEach {
                    println("Commit ${it.commit.name} - ${it.branch}")
                }
    }

    /**
     * Set root element
     * It can be only one for tree
     */
    private fun setRoot(value: RevCommit) {
        val node = Node(value).apply {
            state = NodeState.ROOT
        }
        rootNode = node
        nodes.add(node)
    }

    /**
     * Add ends for tree
     */
    private fun setMirrorCommits(endList: Iterable<RevCommit>) {
        val endNodes = endList.map { Node(it).apply { state = NodeState.END } }

        nodes.removeAll(mirrorCommitNodes)
        mirrorCommitNodes.clear()
        mirrorCommitNodes.addAll(endNodes)
        nodes.addAll(mirrorCommitNodes)
    }

    /**
     * Add element to tree with parents
     */
    private fun add(commit: RevCommit, parentCommits: List<RevCommit>) {
        val node = findNode(commit)
        val parents = parentCommits.map { findOrCreateNode(it) }

        parents.forEach { parent ->
            parent.children.add(node)
            node.parents.add(parent)
        }

        nodes.add(node)
    }

    /**
     * Cut elements that don't belong to root-end lines
     */
    private fun cut() {
        if (mirrorCommitNodes.isEmpty()) throw NoEndsDefineException()

        mirrorCommitNodes.forEach { end ->
            nodes.find {
                it.value.name == end.value.standardHash
            }?.state = NodeState.END
        }

        nodes.removeAll(mirrorCommitNodes)

        val ends = nodes.filter { it.state == NodeState.END }

        val lines: List<List<Node>> = ends.flatMap { end -> buildChain(mutableListOf(end)) }
        val needLines = lines.filter { ends.contains(it.first()) && it.last() == rootNode }

        markNodes(needLines)
    }

    /**
     * Return commits with changes that must be commit
     */
    fun getCommitsWithChanges(): List<RevCommit> = nodes
            .sortedBy { it.value.commitTime }
            .drop(1)
            .map(Node::value)

    /**
     * Return commit to start apply changes, but this commit already exist in mirror
     */
    fun getStandardStartCommit(): RevCommit = if (nodes.isEmpty()) {
        throw StartCommitNotFoundException()
    } else {
        nodes.minBy { it.value.commitTime }!!.value
    }

    fun getMirrorCommitByStandard(standardCommitHash: String): RevCommit = if (mirrorCommitNodes.isEmpty()) {
        throw StartCommitNotFoundException()
    } else {
        mirrorCommitNodes.find {
            it.value.standardHash == standardCommitHash
        }?.value ?: throw StartCommitNotFoundException()
    }

    private fun buildChain(chain: MutableList<Node>): List<List<Node>> {
        val result: MutableList<List<Node>> = mutableListOf()
        var node = chain.last()

        while (true) {
            when (node.children.size) {
                1 -> {
                    val next = node.children.first()
                    chain.add(next)
                    node = next
                }
                0 -> {
                    result.add(chain)
                    return result
                }
                else -> {
                    node.children.forEach {
                        val newChain = chain.toMutableList()
                        newChain.add(it)
                        result.addAll(buildChain(newChain))
                    }
                    return result
                }
            }
        }
    }

    private fun markNodes(lines: List<List<Node>>) {
        lines.flatten()
                .toSet()
                .forEach {
                    if (it.state == NodeState.NONE) it.state = NodeState.MARKED
                }
    }

    private fun findNode(value: RevCommit) = nodes.find { it.value == value }
            ?: throw GitNodeNotFoundException(value)

    private fun findOrCreateNode(value: RevCommit): Node {
        var result = nodes.find { it.value == value }

        if (result == null) {
            result = Node(value)
            nodes.add(result)
        }

        return result
    }

    private data class Node(
            val value: RevCommit,
            var state: NodeState = NodeState.NONE,
            val parents: MutableSet<Node> = mutableSetOf(),
            val children: MutableSet<Node> = mutableSetOf()
    ) {

        override fun hashCode(): Int = value.hashCode()

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (other !is Node) return false
            return value.name == other.value.name
        }

        override fun toString(): String = "${value.name} - $state"
//                "{value:\"$value\", state:\"$state\", parents: \"${parents.map(Node::value)}\", x:\"${children.map(Node::value)}\"}"
    }

    private enum class NodeState {

        NONE, END, ROOT, MARKED
    }

    //WORK WITH BRANCHES
    private fun configureBranches() {
        rootCommitWithBranch = CommitWithBranch(rootNode.value, getRootBranchName())
        var commitWithBranch = rootCommitWithBranch
        while (commitWithBranch.commit.parents.isNotEmpty()) {
            commitWithBranch = handelCommitWithBranch(commitWithBranch) ?: return
        }
    }

    private fun handelCommitWithBranch(commitWithBranch: CommitWithBranch): CommitWithBranch? {
        commitsWithBranch.add(commitWithBranch)

        val parents = findNode(commitWithBranch.commit).parents
        return when (parents.size) {
            0 -> null
            1 -> CommitWithBranch(parents.first().value, commitWithBranch.branch)
            2 -> merge(commitWithBranch)
            else -> throw GitNodeParentException(commitWithBranch.toString())
        }
    }

    private fun merge(mergeCommit: CommitWithBranch): CommitWithBranch {
        val parents = findNode(mergeCommit.commit).parents.toList()

        val firstParent = parents[0]
        val secondParent = parents[1]
        val startMergeCommit = CommitWithBranch(
                findStartMergeCommit(firstParent, secondParent).value,
                mergeCommit.branch
        )

        val firstBranches = findBranch(firstParent.value.name)
        val secondBranches = findBranch(secondParent.value.name)

        var firstCommitWithBranch: CommitWithBranch
        var secondCommitWithBranch: CommitWithBranch

        if (firstBranches.size < secondBranches.size) {
            firstCommitWithBranch = CommitWithBranch(
                    firstParent.value,
                    mergeCommit.branch
            )
            secondCommitWithBranch = CommitWithBranch(
                    secondParent.value,
                    getUnicBranch(mergeCommit, firstBranches, secondBranches)
            )
        } else {
            firstCommitWithBranch = CommitWithBranch(
                    firstParent.value,
                    getUnicBranch(mergeCommit, secondBranches, firstBranches)
            )
            secondCommitWithBranch = CommitWithBranch(
                    secondParent.value,

                    mergeCommit.branch
            )
        }

        while (firstCommitWithBranch.commit != startMergeCommit.commit) {
            firstCommitWithBranch = handelCommitWithBranch(firstCommitWithBranch)
                    ?: return startMergeCommit
        }

        while (secondCommitWithBranch.commit != startMergeCommit.commit) {
            secondCommitWithBranch = handelCommitWithBranch(secondCommitWithBranch)
                    ?: return startMergeCommit
        }

        return startMergeCommit
    }

    private fun getUnicBranch(
            mergeCommit: CommitWithBranch,
            smallList: List<String>,
            bigList: List<String>
    ): String {
        val list = bigList.filter { !smallList.contains(it) }
        if (list.size != 1) {
            throw MergesCommitBranchNotDefinedException(mergeCommit, smallList, bigList)
        }
        return list.first()
    }

    private fun getRootBranchName(): String {
        val id = rootNode.value.name

        var rootBranchName = standardRepository.getBranchById(id)?.name?.substringAfterLast("/")
                ?: EMPTY_STRING

        if (rootBranchName.isEmpty()) rootBranchName = findBranch(id).first()
        if (rootBranchName.isEmpty()) throw BranchNotFoundException(id)

        return rootBranchName
    }

    private fun findBranch(id: String) = standardRepository.getBranchesByContainsId(id)
            .map { it.name.substringAfterLast("/") }
            .distinct()
            .filter { it != HEAD }

    private fun findStartMergeCommit(firstNode: Node, secondNode: Node): Node {
        val firstParents = getAllParentNode(firstNode)
                .filter { it.children.size > 1 }
        val secondParents = getAllParentNode(secondNode)
                .filter { it.children.size > 1 }

        return firstParents.filter { secondParents.contains(it) }.maxBy { it.value.commitTime }!!
    }

    private fun getAllParentNode(node: Node): Set<Node> {
        var parents = node.parents.toList()
        val result = mutableSetOf<Node>().apply {
            addAll(parents)
        }
        while (parents.isNotEmpty()) {
            val praParents = parents.flatMap { it.parents }
            result.addAll(praParents)
            parents = praParents
        }
        return result
    }
}