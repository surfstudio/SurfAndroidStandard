package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.StandardCommit
import java.io.File

/**
 * Work with local standard git repository
 */
class StandardRepository : BaseGitRepository() {

    private val localStandardRepositoryDir = File(".git")

    override val repository: Repository = FileRepositoryBuilder().setGitDir(localStandardRepositoryDir).build()
    override val repositoryName = "Standard"

    override fun getCommit(commitHash: String) = StandardCommit(getRevCommit(commitHash))
}