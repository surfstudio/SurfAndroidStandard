package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.GradleException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.GitNodeNotFoundException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.ManyBranchesFoundException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.MirrorCommitNotFoundByStandardHashException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.NoEndsDefineException
import ru.surfstudio.android.build.tasks.deploy_to_mirror.GitTree.NodeState.*
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitType
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitWithBranch
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.BranchCreator
import ru.surfstudio.android.build.utils.extractBranchNames
import ru.surfstudio.android.build.utils.mirrorStandardHash
import ru.surfstudio.android.build.utils.standardHash
import java.io.File

private const val VERSION_LABEL = "[version]"

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

    private val watchedHashed = mutableSetOf<String>()

    lateinit var startMirrorRepositoryCommits: Set<CommitWithBranch>
    lateinit var standardRepositoryCommitsForMirror: List<CommitWithBranch>

    private lateinit var stopEndNode: Node

    /**
     * Function which checks if commit should be skipped during mirroring
     */
    fun shouldSkipCommit(commit: RevCommit): Boolean =
            commit.shortMessage.endsWith(VERSION_LABEL) &&
                    commit.commitTime < stopEndNode.value.commitTime

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

    /**
     * get start commit from mirror repository
     *
     * @param standardHash hash to find commit by
     *
     * @return commit with corresponded branch from start mirror repository commits
     */
    fun getStartMirrorCommitByStandardHash(standardHash: String): CommitWithBranch {
        return startMirrorRepositoryCommits.find { it.commit.mirrorStandardHash == standardHash }
                ?: throw MirrorCommitNotFoundByStandardHashException(standardHash)
    }

    /**
     * get parent commit for specified commit
     *
     * @param commit commit to get parent for
     *
     * @return parent for commit
     */
    fun getParent(commit: CommitWithBranch): CommitWithBranch? {
        val node = standardNodes.find { it.value == commit.commit }
                ?: throw GitNodeNotFoundException(commit.commit)

        // for a big git tree we can't always find a node parent with a search depth limit
        return standardRepositoryCommitsForMirror.find {
            it.commit == node.parents.firstOrNull()?.value
        }
    }

    /**
     * get parents commits for specified commit for merge
     *
     * @param commit commit to get parents for merge
     *
     * @return parents of specified commit
     */
    fun getMergeParents(commit: CommitWithBranch): List<CommitWithBranch> {
        val node = standardNodes.find { it.value == commit.commit }
                ?: throw GitNodeNotFoundException(commit.commit)

        val parentRevCommits = node.parents.map { it.value }

        return standardRepositoryCommitsForMirror.filter { parentRevCommits.contains(it.commit) }
    }

    /**
     * Add root node to standardNodes list
     *
     * @param value root commit
     */
    private fun createRootNode(value: RevCommit) {
        val node = Node(value).apply {
            state = ROOT
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
        mirrorNodes.addAll(mirrorRevCommits.map { Node(it) }.toSet())
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
            }?.state = END
        }
    }

    /**
     * create mirror repository started commits and standard repository commits
     */
    private fun defineCommits() {
        val lines = createLines()

        buildMirrorStartCommits(lines)
        buildStandardCommitsForMirror(lines)
    }

    /**
     * creates lines: all ways from [rootNode] to end standardNodes
     */
    private fun createLines(): List<List<Node>> {
        markEndNodes()
        val ends = standardNodes
                .filter { it.state == END && it.value.shortMessage.endsWith(VERSION_LABEL) }

        stopEndNode = ends.maxBy { it.value.commitTime }
                ?: throw GradleException("Can't find a stop end node with a min commit time")

        return ends.flatMap { end ->
            watchedHashed.clear()
            buildChain(mutableListOf(end))
        }
                .filter {
                    ends.contains(it.first()) && it.last() == rootNode
                            || it.size > 1 && it.last().value.shortMessage.endsWith(VERSION_LABEL)
                }
    }

    /**
     * creates mirror repository commits which branches models,
     * which are started for applying standard commits afterwards
     *
     * @param lines created lines in standard repository tree
     */
    private fun buildMirrorStartCommits(lines: List<List<Node>>) {
        val mirrorStandardHashes = lines.map { it.first().value.standardHash }

        startMirrorRepositoryCommits = mirrorNodes
                .filter {
                    mirrorStandardHashes.contains(it.value.mirrorStandardHash)
                }
                .map {
                    val branchNameNames =
                            mirrorRepository.getBranchesByContainsId(it.value.name)
                                    .map(Ref::getName)
                                    .extractBranchNames()
                                    .let(::tryResolveBranchConflict)

                    if (branchNameNames.size != 1) {
                        throw ManyBranchesFoundException(it.value.name, branchNameNames)
                    }
                    CommitWithBranch(it.value, branch = branchNameNames[0])
                }
                .toSet()
    }

    /**
     * Try to resolve target branch for push.
     *
     * @param branchNames all branches name with commit
     * @return list contains single item if successfully conflict resolved.
     */
    private fun tryResolveBranchConflict(branchNames: List<String>): List<String> {
        if (branchNames.size < 2) return branchNames

        val repo = FileRepositoryBuilder.create(File(mirrorRepository.repositoryPath, ".git"))
        val revWalk = RevWalk(repo)

        return branchNames.filter { one ->
            branchNames
                    .map { other ->
                        if (one != other) {
                            val a = revWalk.parseCommit(repo.resolve("refs/remotes/origin/$one"))
                            val b = revWalk.parseCommit(repo.resolve("refs/remotes/origin/$other"))
                            revWalk.isMergedInto(a, b).not()
                        } else {
                            true
                        }
                    }
                    .all { it }
        }
    }

    /**
     * creates from lines standard repository commit for applying to mirror. For every line branch name is created
     * Marks every commit either as:
     * MIRROR_START_POINT - commits that are already in mirror repository and applying commits started from
     * SIMPLE - usual commit
     * MERGE - merge commit
     *
     * @param lines created lines in standard repository tree
     */
    private fun buildStandardCommitsForMirror(lines: List<List<Node>>) {
        val existedBranchNames = mirrorRepository.getAllBranches()
                .map { it.name }
                .extractBranchNames()
        val branchName = BranchCreator.generateBranchName(existedBranchNames)

        val mirrorStartCommitsStandardHashes = startMirrorRepositoryCommits.map { it.commit.mirrorStandardHash }

        standardRepositoryCommitsForMirror = standardNodes
                .map {
                    val type = when {
                        mirrorStartCommitsStandardHashes.contains(it.value.standardHash) -> {
                            CommitType.MIRROR_START_POINT
                        }
                        it.parents.size == 2 -> CommitType.MERGE
                        else -> CommitType.SIMPLE
                    }
                    val tags = standardRepository.getTagsForCommit(it.value)
                    CommitWithBranch(commit = it.value, tags = tags, type = type)
                }
                .sortedBy { it.commit.commitTime }

        lines.forEach { line ->
            line.forEach { node ->
                val commit = standardRepositoryCommitsForMirror.find { it.commit == node.value }
                if (commit?.branch?.isEmpty() == true) {
                    commit.branch = branchName
                }
            }
        }

        val mirrorHashes = mirrorNodes.map { it.value.mirrorStandardHash }
        standardRepositoryCommitsForMirror = standardRepositoryCommitsForMirror.filter {
            it.branch.isNotEmpty() && !mirrorHashes.contains(it.commit.standardHash)
        }
    }

    /**
     * creates chain recursively
     */
    private fun buildChain(chain: MutableList<Node>): List<List<Node>> {
        val result: MutableList<List<Node>> = mutableListOf()
        var node = chain.last()
        markAsWatched(node)
        result.add(chain)

        // every line starts and ends with [version] commit
        if (chain.size > 1 && isVersionNode(node)) {
            return result
        }

        node.parents.forEach {
            checkNode(it, chain, result)
        }

        while (true) {
            when (node.children.size) {
                1 -> {
                    val next = node.children.first()

                    next.parents.forEach {
                        checkNode(it, chain, result)
                    }

                    if (!isWatched(next)) {
                        markAsWatched(next)
                        chain.add(next)
                        node = next
                    } else {
                        return result
                    }
                }
                0 -> {
                    result.add(chain)
                    return result
                }
                else -> {
                    node.children.forEach {
                        checkNode(it, chain, result)
                    }
                    return result
                }
            }
        }
    }

    private fun checkNode(node: Node, chain: MutableList<Node>, result: MutableList<List<Node>>) {
        val newChain = chain.toMutableList()
        if (!isWatched(node)) {
            markAsWatched(node)
            newChain.add(node)
            result.addAll(buildChain(newChain))
        } else {
            if (isVersionNode(node) && !newChain.contains(node)) {
                newChain.add(node)
                result.add(newChain)
            }
        }
    }

    private fun isVersionNode(node: Node): Boolean =
            node.value.shortMessage.contains(VERSION_LABEL)

    private fun isWatched(node: Node): Boolean =
            watchedHashed.contains(node.value.standardHash)

    private fun markAsWatched(node: Node) {
        watchedHashed.add(node.value.standardHash)
    }

    /**
     * represents information about commit and its parents
     */
    private data class Node(
            val value: RevCommit,
            var state: NodeState = NONE,
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