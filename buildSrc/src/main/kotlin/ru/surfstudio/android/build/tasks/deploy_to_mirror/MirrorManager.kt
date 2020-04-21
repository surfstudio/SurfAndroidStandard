package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.GradleException
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.RevCommitNotFoundException
import ru.surfstudio.android.build.tasks.changed_components.CommandLineRunner
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitType
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitWithBranch
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.extractBranchNames
import ru.surfstudio.android.build.utils.standardHash

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

    private val mirrorDir = mirrorRepository.repositoryPath
    private val standardDir = standardRepository.repositoryPath

    private val gitTree = GitTree(standardRepository, mirrorRepository)

    private val filesToMirror = listOf(
            ".gitignore",
            "build.gradle",
            "gradle.properties",
            "gradlew",
            "gradlew.bat",
            "settings.gradle"
    )
    private val foldersToMirror = listOf(
            componentDirectory,
            "buildSrc",
            "common", //todo mirror if needed
            "gradle"
    )
    private val uniqueMirrorFiles = listOf(
            ".git",
            "README.md",
            "mirror.properties"
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
            gitTree.buildGitTree(rootCommit, standardCommits, mirrorCommits)
            applyGitTreeToMirror()
            setBranches()
            //mirrorRepository.push()
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
        gitTree.standardRepositoryCommitsForMirror.forEach { commit ->
            println("${commit.commit.standardHash} ${commit.type} ${commit.commit.shortMessage}")
            (when (commit.type) {
                CommitType.SIMPLE, CommitType.MERGE -> commit(commit)
                CommitType.MIRROR_START_POINT -> latestMirrorCommit
                else -> null
            })?.let { commit.tags.forEach { tag -> mirrorRepository.tag(it, tag) } }
        }
    }

    /**
     * creates commit in mirror repository by
     * getting all changes for [commit]
     * in standard repository and applying them to mirror repository
     *
     * @param commit commit to apply
     */
    private fun commit(commit: CommitWithBranch): RevCommit? {
        // for old branches a line could contain an old [version] commit
        if (gitTree.shouldSkipCommit(commit.commit)) {
            return null
        }

        standardRepository.reset(commit.commit)
        checkoutMirrorBranchForCommit(commit)

        // remove files in mirror repo to owerwrite them afterwards
        mirrorDir.listFiles()?.filter {
            !uniqueMirrorFiles.contains(it.name)
        }?.forEach {
            it.deleteRecursively()
        }

        // copy standard files to mirror repo using given commit revision
        standardDir.listFiles()?.filter {
            filesToMirror.contains(it.name) || foldersToMirror.contains(it.name)
        }?.forEach {
            CommandLineRunner.runCommandWithResult(
                    command = "cp -r ../${standardDir.name}/${it.name} .",
                    workingDir = mirrorDir
            )
        }

        mirrorRepository.add()
        val newCommit = mirrorRepository.commit(commit.commit)
        commit.mirrorCommitHash = newCommit?.name ?: EMPTY_STRING
        commit.type = CommitType.COMMITED
        return newCommit
    }

    /**
     * handles checkout right branch for a new commit
     *
     * @param commit commit
     */
    private fun checkoutMirrorBranchForCommit(commit: CommitWithBranch) {
        with(mirrorRepository) {
            gitTree.getParent(commit)?.also { safeParent ->
                if (safeParent.mirrorCommitHash.isNotEmpty()) {
                    checkoutCommit(safeParent.mirrorCommitHash)
                }
                if (commit.branch.isNotEmpty()) {
                    checkoutBranch(commit.branch)
                }
            }
        }
    }
}