package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import java.io.File

/**
 * Work with local standard git repository
 */
class StandardRepository : BaseGitRepository() {

    override val repositoryPath: File = File("")
    override val repositoryName = "Standard"
}