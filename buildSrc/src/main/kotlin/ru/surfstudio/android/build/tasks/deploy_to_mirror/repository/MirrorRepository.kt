package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import ru.surfstudio.android.build.exceptions.deploy_to_mirror.MirrorCommitNotFoundByStandardHashException
import ru.surfstudio.android.build.utils.STANDARD_COMMIT_HASH_POSTFIX
import ru.surfstudio.android.build.utils.STANDARD_COMMIT_HASH_PREFIX
import ru.surfstudio.android.build.utils.mirrorStandardHash
import ru.surfstudio.android.build.utils.shortHash
import java.io.File

/**
 * Work with local mirror git repository
 */
class MirrorRepository(dirPath: String) : BaseGitRepository() {

    override val repositoryPath = File(dirPath)

    override val repositoryName = "Mirror"

    fun commit(commit: RevCommit): RevCommit? {
        val resultCommit = git.commit()
                .setAuthor(commit.authorIdent)
                .setAll(true)
                .setMessage("${commit.shortMessage} $STANDARD_COMMIT_HASH_PREFIX${commit.shortHash}$STANDARD_COMMIT_HASH_POSTFIX")
                .call()
        return resultCommit
    }

    fun push() {
        git.push()
                .setCredentialsProvider(
                        UsernamePasswordCredentialsProvider(
                                GithubConfig.USERNAME,
                                GithubConfig.PASSWORD
                        )
                )
                .call()
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
}