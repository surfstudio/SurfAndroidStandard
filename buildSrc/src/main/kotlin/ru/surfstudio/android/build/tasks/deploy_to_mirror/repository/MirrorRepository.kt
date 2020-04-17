package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.dircache.DirCache
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.MirrorCommitNotFoundByStandardHashException
import ru.surfstudio.android.build.tasks.changed_components.CommandLineRunner
import ru.surfstudio.android.build.utils.STANDARD_COMMIT_HASH_POSTFIX
import ru.surfstudio.android.build.utils.STANDARD_COMMIT_HASH_PREFIX
import ru.surfstudio.android.build.utils.mirrorStandardHash
import ru.surfstudio.android.build.utils.shortHash
import java.io.File
import java.net.URLEncoder

private const val HTTPS_PREFIX = "https://"
private const val GIT_SUFFIX = ".git"
private const val UTF_8_ENCODER = "UTF-8"

/**
 * Work with local mirror git repository
 */
class MirrorRepository(
        dirPath: String,
        private val mirrorUrl: String
) : BaseGitRepository() {

    override val repositoryPath = File(dirPath)

    override val repositoryName = "Mirror"

    fun add(filePattern: String = "."): DirCache {
        return git.add()
                .addFilepattern(filePattern)
                .call()
    }

    fun commit(commit: RevCommit): RevCommit? {
        return git.commit()
                .setAuthor(commit.authorIdent)
                .setAll(true)
                .setMessage("${commit.shortMessage} $STANDARD_COMMIT_HASH_PREFIX${commit.shortHash}$STANDARD_COMMIT_HASH_POSTFIX")
                .call()
    }

    fun push() {
        CommandLineRunner.runCommandWithResult(
                command = "git push ${getPushUrl()}",
                workingDir = repositoryPath
        )
    }

    fun tag(commit: RevCommit, tagName: String) = git.tag()
            .setObjectId(commit)
            .setName(tagName)
            .call()

    fun getCommitByStandardHash(standardHash: String): RevCommit = git.log()
            .all()
            .call()
            .find { it.mirrorStandardHash == standardHash }
            ?: throw MirrorCommitNotFoundByStandardHashException(standardHash)

    private fun getPushUrl(): String {
        return StringBuilder(mirrorUrl)
                .insert(
                        HTTPS_PREFIX.length,
                        "${encodeString(GithubConfig.USERNAME)}:${encodeString(GithubConfig.PASSWORD)}@"
                ).append(GIT_SUFFIX)
                .toString()
    }

    private fun encodeString(string: String): String =
            URLEncoder.encode(string, UTF_8_ENCODER)
}