package ru.surfstudio.android.build.tasks.deploy_to_mirror.repository

import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import ru.surfstudio.android.build.utils.EMPTY_STRING
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

    private val tags: List<Ref> by lazy { git.tagList().call() }

    fun getTagsForCommit(commit: RevCommit): List<String> =
            tags.filter { it.objectId == commit }
                    .map { it.name.replace(Constants.R_TAGS, EMPTY_STRING) }
}