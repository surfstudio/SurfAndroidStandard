package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.GitNodeNotFoundException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.NoEndsDefineException
import ru.surfstudio.android.build.utils.standardHash
import kotlin.collections.ArrayList

/**
 * Data structure based on tree
 * Use it to build tree, set root and ends elements and delete other
 */
class GitTree {

    private var root: Node? = null
    private val mirrorCommits: MutableSet<Node> = mutableSetOf()
    private val list: MutableSet<Node> = mutableSetOf()

    /**
     * Set root element
     * It can be only one for tree
     */
    fun setRoot(value: RevCommit) {
        val node = Node(value).apply {
            state = NodeState.ROOT
        }
        root = node
        list.add(node)
    }

    /**
     * Add ends for tree
     */
    fun setEnds(endList: Iterable<RevCommit>) {
        val endNodes = endList.map { Node(it).apply { state = NodeState.END } }

        list.removeAll(mirrorCommits)
        mirrorCommits.clear()
        mirrorCommits.addAll(endNodes)
        list.addAll(mirrorCommits)
    }

    /**
     * Add element to tree with parents
     */
    fun add(commit: RevCommit, parentCommits: List<RevCommit>) {
        val node = findNode(commit)
        val parents = parentCommits.map { findOrCreateNode(it) }

        parents.forEach { parent ->
            parent.children.add(node)
            node.parents.add(parent)
        }

        list.add(node)
    }

    /**
     * Cut elements that don't belong to root-end lines
     */
    fun cut() {
        if (mirrorCommits.isEmpty()) throw NoEndsDefineException()

        mirrorCommits.forEach { end ->
            list.find {
                it.value.name == end.value.standardHash
            }?.state = NodeState.END
        }

        list.removeAll(mirrorCommits)

        val ends = list.filter { it.state == NodeState.END }

        val lines: List<List<Node>> = ends.flatMap { end -> buildChain(mutableListOf(end)) }
        val needLines = lines.filter { ends.contains(it.first()) && it.last() == root }

        markNodes(needLines)
    }

    fun print() {
        println(list)
    }

    private fun buildChain(chain: MutableList<Node>): List<List<Node>> {
        val result: MutableList<List<Node>> = mutableListOf()
        var node = chain.last()

        while (true) {
            when (node.children.size) {
                1 -> {
                    val parent = node.children.first()
                    chain.add(parent)
                    node = parent
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

    private fun findNode(value: RevCommit) = list.find { it.value == value }
            ?: throw GitNodeNotFoundException(value)

    private fun findOrCreateNode(value: RevCommit): Node {
        var result = list.find { it.value == value }

        if (result == null) {
            result = Node(value)
            list.add(result)
        }

        return result
    }

    private data class Node(
            val value: RevCommit,
            var state: NodeState = NodeState.NONE,
            val parents: ArrayList<Node> = arrayListOf(),
            val children: ArrayList<Node> = arrayListOf()
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