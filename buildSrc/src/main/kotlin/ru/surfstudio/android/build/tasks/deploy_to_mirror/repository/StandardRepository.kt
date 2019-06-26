package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import java.io.File

/**
 * Work with local standard git repository
 */
class StandardRepository : BaseGitRepository() {

    companion object {
        const val TEMP_DIR_PATH = "temp"
    }

    override val repositoryPath: File = File(TEMP_DIR_PATH)
    override val repositoryName = "Standard"
}