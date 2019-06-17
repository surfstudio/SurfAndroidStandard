package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File

/**
 * Work with local mirror git repository
 */
class LocalMirrorRepository(url: String) : BaseGitRepository() {

    override val git: Git
    override val repositoryName = "Mirror"

    private val userName: String = System.getenv("surf_maven_username") ?: EMPTY_STRING
    private val password: String = System.getenv("surf_maven_password") ?: EMPTY_STRING

    private val localMirrorDir = File(".mirror")

    init {
        Git.cloneRepository()
                .setURI(url)
                .setDirectory(localMirrorDir)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(userName, password))
                .call()

        git = Git(FileRepositoryBuilder().setGitDir(localMirrorDir).build())
    }

    override fun delete() {
        FileUtils.deleteDirectory(localMirrorDir)
    }
}