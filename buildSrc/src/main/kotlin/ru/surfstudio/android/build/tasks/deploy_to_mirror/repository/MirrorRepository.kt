package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File

const val MIRROR_REPOSITORY_PATH = ".mirror"

/**
 * Work with local mirror git repository
 */
class MirrorRepository(url: String) : BaseGitRepository() {

    override val repositoryPath = File(MIRROR_REPOSITORY_PATH)
    override val repositoryName = "Mirror"

    private val userName: String = System.getenv("surf_maven_username") ?: EMPTY_STRING
    private val password: String = System.getenv("surf_maven_password") ?: EMPTY_STRING

    init {
        Git.cloneRepository()
                .setURI(url)
                .setDirectory(repositoryPath)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(userName, password))
                .call()
    }

    override fun delete() {
        super.delete()
        FileUtils.deleteDirectory(repositoryPath)
    }
}