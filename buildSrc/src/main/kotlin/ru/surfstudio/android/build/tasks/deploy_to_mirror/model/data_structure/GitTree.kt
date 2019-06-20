package ru.surfstudio.android.build.tasks.deploy_to_mirror.model.data_structure

import ru.surfstudio.android.build.exceptions.deploy_to_mirror.GitNodeNotFoundException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.NoEndsDefineException
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.Commit
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.MirrorCommit
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.StandardCommit
import kotlin.collections.ArrayList

/**
 * Data structure based on tree
 * Use it to build tree, set root and ends elements and delete other
 */
class GitTree {

    private var root: Node? = null
    private val ends: MutableSet<Node> = mutableSetOf()
    private val list: MutableSet<Node> = mutableSetOf()

    /**
     * Set root element
     * It can be only one for tree
     */
    fun setRoot(value: StandardCommit) {
        val node = Node(value).apply {
            state = NodeState.ROOT
        }
        root = node
        list.add(node)
    }

    /**
     * Add ends for tree
     */
    fun setEnds(collection: Collection<MirrorCommit>) {
        val endNodes = collection.map { Node(it).apply { state = NodeState.END } }

        list.removeAll(ends)
        ends.clear()
        ends.addAll(endNodes)
        list.addAll(ends)
    }

    /**
     * Add element to tree with parents
     */
    fun add(commit: Commit, parentCommits: List<Commit>) {
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
        if (ends.isEmpty()) throw NoEndsDefineException()

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
            when (node.parents.size) {
                1 -> {
                    val parent = node.parents.first()
                    chain.add(parent)
                    node = parent
                }
                0 -> {
                    result.add(chain)
                    return result
                }
                else -> {
                    node.parents.forEach {
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
                    if (it.state == NodeState.NONE) it.state == NodeState.MARKED
                }
    }

    private fun findNode(value: Commit) = list.find { it.value == value }
            ?: throw GitNodeNotFoundException(value)

    private fun findOrCreateNode(value: Commit) = list.find { it.value == value } ?: Node(value)

    private data class Node(
            val value: Commit,
            var state: NodeState = NodeState.NONE,
            val parents: ArrayList<Node> = arrayListOf(),
            val children: ArrayList<Node> = arrayListOf()
    ) {
        override fun toString(): String =
                "{value:\"$value\", state:\"$state\", parents: \"${parents.map(Node::value)}\", x:\"${children.map(Node::value)}\"}"
    }

    private enum class NodeState {

        NONE, END, ROOT, MARKED
    }
}