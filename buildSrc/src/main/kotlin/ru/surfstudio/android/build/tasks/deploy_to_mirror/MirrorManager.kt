package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.GradleException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.RevCommitNotFoundException
import ru.surfstudio.android.build.tasks.changed_components.CommandLineRunner
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitType
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitWithBranch
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val GET_MAIN_BRANCH_COMMAND = "git symbolic-ref refs/remotes/origin/HEAD"

/**
 * Class for mirroring android standard to mirror repository
 * Only [filesToMirror] and [foldersToMirror] are mirrored
 */
class MirrorManager(
        componentDirectory: String,
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

    private var latestMirrorCommit: RevCommit? = null

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

        val mainBranchFullName = CommandLineRunner.runCommandWithResult(
                command = GET_MAIN_BRANCH_COMMAND,
                workingDir = mirrorRepository.repositoryPath
        )?.trim()

        if (mainBranchFullName != null) {
            val mainBranch = mirrorRepository.getAllBranches()
                    .first { it.name == mainBranchFullName }

            val mirrorCommits: Set<RevCommit> = mirrorRepository.getAllCommits(
                    mainBranch.objectId.name, mirrorDepthLimit)
                    .toSet()

            latestMirrorCommit = mirrorCommits.maxBy { it.commitTime }

            latestMirrorCommit?.also { safeLatestMirrorCommit ->
                println("latestMirrorCommit ${safeLatestMirrorCommit.shortMessage}")
                if (safeLatestMirrorCommit.commitTime > rootCommit.commitTime) {
                    throw GradleException("Invalid mirror commit $rootCommitHash: " +
                            "can't be earlier than latest mirror commit ${safeLatestMirrorCommit.standardHash}")
                }

                gitTree.buildGitTree(rootCommit, standardCommits, mirrorCommits)
                applyGitTreeToMirror()
                setBranches()
                //mirrorRepository.push()
                return
            }
            throw GradleException("Can't get latest commit in branch $mainBranchFullName " +
                    "for repo ${mirrorRepository.repositoryName}")
        } else {
            throw GradleException("Can't get main branch " +
                    "for repo ${mirrorRepository.repositoryName}")
        }
    }

    private fun setBranches() {
        val commitToSetBranches = gitTree.standardRepositoryCommitsForMirror.lastOrNull { it.type == CommitType.COMMITED }
                ?: return
        val branchesToCreate = standardRepository.getBranchesByContainsId(commitToSetBranches.commit.name)
                .map(Ref::getName)
                .extractBranchNames()
        branchesToCreate.forEach { branch ->
            mirrorRepository.createBranch(branch, commitToSetBranches.mirrorCommitHash)
        }
        mirrorRepository.checkoutBranch(branchesToCreate.first())
        gitTree.standardRepositoryCommitsForMirror
                .map { it.branch }
                .toSet()
                .forEach { mirrorRepository.deleteBranch(it) }
    }

    /**
     * For all git tree commits apply them to mirror repository
     */
    private fun applyGitTreeToMirror() {
        val index = gitTree.standardRepositoryCommitsForMirror.indexOfFirst {
            it.type == CommitType.MIRROR_START_POINT
        }
        val size = gitTree.standardRepositoryCommitsForMirror.size
        gitTree.standardRepositoryCommitsForMirror.forEach {
            //println("sublist ${it.commit.shortMessage}")
        }
        println()
        gitTree.standardRepositoryCommitsForMirror.forEach { commit ->
            val date = Date(1000L * commit.commit.commitTime)
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    .format(date)
            println("commit ${commit.type} ${commit.commit.shortMessage} $format")
            (when (commit.type) {
                CommitType.SIMPLE -> commit(commit)
                CommitType.MERGE -> merge(commit)
                CommitType.MIRROR_START_POINT -> latestMirrorCommit
                else -> null
            })?.let { commit.tags.forEach { tag -> mirrorRepository.tag(it, tag) } }
        }
    }

    /**
     * creates start commit of git tree in mirror repository
     *
     * @param commit start commit
     */
    private fun createMirrorStartCommit(commit: CommitWithBranch): RevCommit {
        val mirrorCommit = gitTree.getStartMirrorCommitByStandardHash(commit.commit.standardHash)
        println("mirrorCommit ${mirrorCommit.commit.shortMessage}")
        mirrorRepository.reset(mirrorCommit.commit)
        mirrorRepository.checkoutBranch(mirrorCommit.branch)
        commit.mirrorCommitHash = mirrorCommit.commit.name
        return mirrorCommit.commit
    }

    /**
     * creates commit in mirror repository by
     * getting all changes for [commit]
     * in standard repository and applying them to mirror repository
     *
     * @param commit commit to apply
     */
    private fun commit(commit: CommitWithBranch): RevCommit? {
        standardRepository.reset(commit.commit)

        val changes = standardRepository.getChanges(commit.commit).filter(::shouldMirror)
        if (changes.isEmpty()) return null

        checkoutMirrorBranchForCommit(commit)
        applyChanges(changes)
        val newCommit = mirrorRepository.commit(commit.commit)
        commit.mirrorCommitHash = newCommit?.name ?: EMPTY_STRING
        commit.type = CommitType.COMMITED
        return newCommit
    }

    /**
     * handles checkout right branch for new commit
     *
     * @param commit commit
     */
    private fun checkoutMirrorBranchForCommit(commit: CommitWithBranch) {
        with(mirrorRepository) {
            val parent = gitTree.getParent(commit)
            if (parent.mirrorCommitHash.isNotEmpty()) {
                checkoutCommit(parent.mirrorCommitHash)
            }
            if (commit.branch.isNotEmpty()) {
                checkoutBranch(commit.branch)
            }
        }
    }

    /**
     * creates merge commit by getting merge parents for commit
     * and merging them.
     * In case of conflicts just copies file from standard repository to mirror repository
     *
     * todo in case if mirror repo contains unique commits, its content must not be overridden
     *
     * @param commit commit to apply
     */
    private fun merge(commit: CommitWithBranch): RevCommit? {
        standardRepository.reset(commit.commit)

        val mainBranch = commit.branch
        val secondBranch = gitTree.getMergeParents(commit)
                .map(CommitWithBranch::branch)
                .firstOrNull { it != mainBranch }
                ?: return null

        if (!mirrorRepository.isBranchExists(mainBranch) || !mirrorRepository.isBranchExists(secondBranch)) return null

        mirrorRepository.checkoutBranch(mainBranch)
        val conflicts = mirrorRepository.merge(secondBranch)
        conflicts.forEach {
            val filePath = it.replaceFirst("${mirrorRepository.repositoryPath.path}/", EMPTY_STRING)
            diffManager.modify(filePath)
        }

        val newCommit = mirrorRepository.commit(commit.commit)
        commit.mirrorCommitHash = newCommit?.name ?: EMPTY_STRING
        commit.type = CommitType.COMMITED
        return newCommit
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