package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.diff.DiffEntry
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MIRROR_REPOSITORY_PATH
import java.io.File

object GitDiffManager {

    fun add(diffEntry: DiffEntry) {
        val standardFile = File(diffEntry.newPath)
        val mirrorFile = File("$MIRROR_REPOSITORY_PATH/${diffEntry.newPath}")

        FileUtils.copyFile(standardFile, mirrorFile)
    }

    fun copy(diffEntry: DiffEntry) {
        val copyFrom = File("$MIRROR_REPOSITORY_PATH/${diffEntry.oldPath}")
        val copyTo = File("$MIRROR_REPOSITORY_PATH/${diffEntry.newPath}")

        FileUtils.copyFile(copyFrom, copyTo)
    }

    fun delete(diffEntry: DiffEntry) {
        val file = File("$MIRROR_REPOSITORY_PATH/${diffEntry.oldPath}")

        FileUtils.forceDelete(file)
    }

    fun modify(diffEntry: DiffEntry) {
        val standardFile = File(diffEntry.oldPath)
        val mirrorFile = File("$MIRROR_REPOSITORY_PATH/${diffEntry.oldPath}")

        FileUtils.copyFile(standardFile, mirrorFile)
    }

    fun rename(diffEntry: DiffEntry) {
        val oldFile = File("$MIRROR_REPOSITORY_PATH/${diffEntry.oldPath}")
        val newFile = File("$MIRROR_REPOSITORY_PATH/${diffEntry.newPath}")

        FileUtils.copyFile(oldFile, newFile)
        FileUtils.forceDelete(oldFile)
    }
}