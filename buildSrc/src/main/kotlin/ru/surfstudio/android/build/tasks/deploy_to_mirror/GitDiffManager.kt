package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.diff.DiffEntry
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import java.io.File

class GitDiffManager(
        private val standardDir: String,
        private val mirrorDir: String
) {

    fun add(diffEntry: DiffEntry) {
        val standardFile = File(diffEntry.newPath)
        val mirrorFile = File(chanceRepoDir(diffEntry.newPath))

        FileUtils.copyFile(standardFile, mirrorFile)
    }

    fun copy(diffEntry: DiffEntry) {
        val copyFrom = File(chanceRepoDir(diffEntry.oldPath))
        val copyTo = File(chanceRepoDir(diffEntry.newPath))

        FileUtils.copyFile(copyFrom, copyTo)
    }

    fun delete(diffEntry: DiffEntry) {
        val file = File(chanceRepoDir(diffEntry.oldPath))

        FileUtils.forceDelete(file)
    }

    fun modify(diffEntry: DiffEntry) {
        val standardFile = File(diffEntry.oldPath)
        val mirrorFile = File(chanceRepoDir(diffEntry.oldPath))

        FileUtils.copyFile(standardFile, mirrorFile)
    }

    fun rename(diffEntry: DiffEntry) {
        val oldFile = File(chanceRepoDir(diffEntry.oldPath))
        val newFile = File(chanceRepoDir(diffEntry.newPath))

        FileUtils.copyFile(oldFile, newFile)
        FileUtils.forceDelete(oldFile)
    }

    private fun chanceRepoDir(path: String): String {
        return if (path.startsWith(StandardRepository.TEMP_DIR_PATH)) {
            path.replaceBefore("/", mirrorDir)
        } else {
            path.replaceBefore("/", standardDir)
        }
    }
}