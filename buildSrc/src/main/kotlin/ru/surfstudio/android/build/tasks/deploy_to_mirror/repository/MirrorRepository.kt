package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File

/**
 * Work with local mirror git repository
 */
class MirrorRepository(url: String) : BaseGitRepository() {

    override val repository: Repository
    override val repositoryName = "Mirror"

    private val userName: String = System.getenv("surf_maven_username") ?: EMPTY_STRING
    private val password: String = System.getenv("surf_maven_password") ?: EMPTY_STRING

    private val localMirrorDir = File(".mirror")
    private val localMirrorGitDir = File("${localMirrorDir.path}/.git")

    init {
        Git.cloneRepository()
                .setURI(url)
                .setDirectory(localMirrorDir)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(userName, password))
                .call()

        repository = FileRepositoryBuilder().setGitDir(localMirrorGitDir).build()
    }

    override fun delete() {
        super.delete()
        FileUtils.deleteDirectory(localMirrorDir)
    }
}