package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.StartCommitNotFoundException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.GitNodeNotFoundException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.NoEndsDefineException
import ru.surfstudio.android.build.utils.standardHash

/**
 * Data structure based on tree
 * Use it to build tree, set root and ends elements and delete other
 */
class GitTree {

    private lateinit var rootNode: Node
    private val mirrorCommitNodes: MutableSet<Node> = mutableSetOf()
    private val nodes: MutableSet<Node> = mutableSetOf()

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
}