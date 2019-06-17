package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

/**
 * Work with local standard git repository
 */
class StandardRepository : BaseGitRepository() {

    private val localStandardRepositoryDir = File(".git")

    override val git: Git = Git(
            FileRepositoryBuilder().setGitDir(localStandardRepositoryDir).build()
    )
    override val repositoryName = "Standard"

    override fun delete() {
        // Do nothing
    }
}