package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.GitNodeNotFoundException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.ManyBranchesFoundException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.MirrorCommitNotFoundByStandardHashException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.NoEndsDefineException
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitType
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitWithBranch
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.BranchCreator
import ru.surfstudio.android.build.utils.extractBranchNames
import ru.surfstudio.android.build.utils.mirrorStandardHash
import ru.surfstudio.android.build.utils.standardHash

/**
 * Data structure based on tree
 * Use it to build tree, set root and ends elements and delete other
 */
class GitTree(
        private val standardRepository: StandardRepository,
        private val mirrorRepository: MirrorRepository
) {

    private lateinit var rootNode: Node
    private val mirrorNodes: MutableSet<Node> = mutableSetOf()
    private val standardNodes: MutableSet<Node> = mutableSetOf()

    lateinit var mirrorStartCommits: Set<CommitWithBranch>
    lateinit var standardCommitsForMirror: List<CommitWithBranch>

    /**
     * Build GitTree representation of standard repository with correct structure
     */
    fun buildGitTree(
            rootRevCommit: RevCommit,
            standardRevCommits: Iterable<RevCommit>,
            mirrorRevCommits: Iterable<RevCommit>
    ) {
        createRootNode(rootRevCommit)
        createMirrorNodes(mirrorRevCommits)
        createStandardNodes(standardRevCommits)

        defineCommits()
    }

    fun getStartMirrorCommitByStandardHash(standardHash: String): CommitWithBranch {
        return mirrorStartCommits.find { it.commit.mirrorStandardHash == standardHash }
                ?: throw MirrorCommitNotFoundByStandardHashException(standardHash)
    }

    fun getParent(commit: CommitWithBranch): CommitWithBranch {
        val node = standardNodes.find { it.value == commit.commit }
                ?: throw GitNodeNotFoundException(commit.commit)
        return standardCommitsForMirror.find { it.commit == node.parents.first().value }
                ?: throw GitNodeNotFoundException(node.value)
    }

    fun getMergeParents(commit: CommitWithBranch): List<CommitWithBranch> {
        val node = standardNodes.find { it.value == commit.commit }
                ?: throw GitNodeNotFoundException(commit.commit)

        val parentRevCommits = node.parents.map { it.value }

        return standardCommitsForMirror.filter { parentRevCommits.contains(it.commit) }
    }

    /**
     * Add root node to standardNodes list
     *
     * @param value root commit
     */
    private fun createRootNode(value: RevCommit) {
        val node = Node(value).apply {
            state = NodeState.ROOT
        }
        rootNode = node
        standardNodes.add(node)
    }

    /**
     * Add commits from mirror repository as mirror standardNodes
     *
     * @param mirrorRevCommits commits from mirror repository
     */
    private fun createMirrorNodes(mirrorRevCommits: Iterable<RevCommit>) {
        mirrorNodes.addAll(mirrorRevCommits.map { Node(it)}.toSet())
        if (mirrorNodes.isEmpty()) throw NoEndsDefineException()
    }

    /**
     * Finds all commits from standard repository that mirror doesnt have and adds them to standardNodes list
     * Starts from [rootNode], every iteration consists of taking parents of current node,
     * adding them to standardNodes list until meet commit from [mirrorNodes].
     *
     * @param standardRevCommits commits from standard repository
     */
    private fun createStandardNodes(standardRevCommits: Iterable<RevCommit>) {
        val c = mutableSetOf(rootNode.value)
        val p = mutableSetOf<RevCommit>()

        val mirrorStandardHashes = mirrorNodes.map { it.value.mirrorStandardHash }

        for (standardCommit in standardRevCommits) {
            c.forEach { commit ->
                val parentHashes = commit.parents.map { it.name }
                val parents = standardRevCommits.filter { parentHashes.contains(it.name) }

                addRevCommitToNodes(commit, parents)

                p.addAll(
                        parents.filter {
                            !mirrorStandardHashes.contains(it.standardHash)
                        }
                )
            }

            c.clear()
            c.addAll(p)
            p.clear()

            if (c.isEmpty()) break
        }
    }

    /**
     * adds commit to corresponded node and sets parents and children
     *
     * @param commit commit to add
     * @param parentCommits parents of commit
     */
    private fun addRevCommitToNodes(commit: RevCommit, parentCommits: List<RevCommit>) {
        val node = findNode(commit)
        val parents = parentCommits.map { findOrCreateNode(it) }

        parents.forEach { parent ->
            parent.children.add(node)
            node.parents.add(parent)
        }

        standardNodes.add(node)
    }

    /**
     * get node from [standardNodes] by commit
     *
     * @param value commit
     *
     *  @return node
     */
    private fun findNode(value: RevCommit) = standardNodes.find { it.value == value }
            ?: throw GitNodeNotFoundException(value)

    /**
     * gets node from standardNodes or creates if not found
     *
     * @param value commit
     *
     * @return node
     */
    private fun findOrCreateNode(value: RevCommit): Node {
        var result = standardNodes.find { it.value == value }

        if (result == null) {
            result = Node(value)
            standardNodes.add(result)
        }

        return result
    }

    /**
     * mark all standardNodes in standardNodes list, that have mirrorStandardHash contained in mirrorNodes as END standardNodes
     */
    private fun markEndNodes() {
        mirrorNodes.forEach { mirrorNode ->
            standardNodes.find {
                it.value.standardHash == mirrorNode.value.mirrorStandardHash
            }?.state = NodeState.END
        }
    }

    /**
     * create commits models
     */
    private fun defineCommits(){
        val lines = createLines()

        buildMirrorStartCommits(lines)
        buildStandardCommitsForMirror(lines)
    }

    /**
     * creates lines: all ways from [rootNode] to end standardNodes
     */
    private fun createLines(): List<List<Node>> {
        markEndNodes()

        val ends = standardNodes.filter { it.state == NodeState.END }

        return ends.flatMap { end -> buildChain(mutableListOf(end)) }
                .filter { ends.contains(it.first()) && it.last() == rootNode }
    }

    private fun buildMirrorStartCommits(lines: List<List<Node>>) {
        val mirrorStandardHashes = lines.map { it.first().value.standardHash }

        mirrorStartCommits = mirrorNodes
                .filter {
                    mirrorStandardHashes.contains(it.value.mirrorStandardHash)
                }
                .map {
                    val branchNameNames = mirrorRepository.getBranchesByContainsId(it.value.name)
                            .map(Ref::getName)
                            .extractBranchNames()

                    if (branchNameNames.size != 1) {
                        throw ManyBranchesFoundException(it.value.name, branchNameNames)
                    }
                    CommitWithBranch(it.value, branchNameNames[0])
                }.toSet()
    }

    private fun buildStandardCommitsForMirror(lines: List<List<Node>>) {
        val existedBranchNames = mirrorRepository.getAllBranches()
                .map { it.name }
                .extractBranchNames()

        val mirrorStartCommitsStandardHashes = mirrorStartCommits.map { it.commit.mirrorStandardHash }

        standardCommitsForMirror = standardNodes
                .map {
                    val type = when {
                        mirrorStartCommitsStandardHashes.contains(it.value.standardHash) -> {
                            CommitType.MIRROR_START_POINT
                        }
                        it.parents.size == 2 -> CommitType.MERGE
                        else -> CommitType.SIMPLE
                    }
                    CommitWithBranch(commit = it.value, type = type)
                }
                .sortedBy { it.commit.commitTime }

        lines.forEach { line ->
            val branchName = BranchCreator.generateBranchName(existedBranchNames)
            line.forEach { node ->
                val commit = standardCommitsForMirror.find { it.commit == node.value }
                if (commit?.branch?.isEmpty() == true) commit.branch = branchName
            }
        }
    }

    /**
     * creates chain recursively
     */
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

    /**
     * represents information about commit and its parents
     */
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

        override fun toString(): String = "${value.shortMessage} - $state"
    }

    /**
     * State of node:
     * [NONE] - simple node
     * [END] - nodes that already have corresponded nodes in mirror repository
     * [ROOT] - node that corresponds commit hash in task parameter
     */
    private enum class NodeState {

        NONE, END, ROOT
    }
}