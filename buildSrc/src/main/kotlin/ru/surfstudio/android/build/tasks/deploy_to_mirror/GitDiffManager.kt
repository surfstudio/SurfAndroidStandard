package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.diff.DiffEntry
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import java.io.File

class GitDiffManager(
        private val standardDir: String,
        private val mirrorRepository: MirrorRepository
) {

    fun add(diffEntry: DiffEntry) {
        val standardFile = standardPath(diffEntry.newPath)
        val mirrorFile = mirrorPath(diffEntry.newPath)

        FileUtils.copyFile(standardFile, mirrorFile)

        mirrorRepository.addToIndex(mirrorFile.path)
    }

    fun copy(diffEntry: DiffEntry) {
        val copyFrom = mirrorPath(diffEntry.oldPath)
        val copyTo = mirrorPath(diffEntry.newPath)

        FileUtils.copyFile(copyFrom, copyTo)

        mirrorRepository.addToIndex(copyTo.path)
    }

    fun delete(diffEntry: DiffEntry) {
        val file = mirrorPath(diffEntry.oldPath)

        FileUtils.forceDelete(file)

        mirrorRepository.addToIndex(file.path)
    }

    fun modify(diffEntry: DiffEntry) = modify(diffEntry.oldPath)


    fun modify(filePath: String) {
        val standardFile = standardPath(filePath)
        val mirrorFile = mirrorPath(filePath)

        FileUtils.copyFile(standardFile, mirrorFile)

        mirrorRepository.addToIndex(mirrorFile.path)
    }

    fun rename(diffEntry: DiffEntry) {
        val oldFile = mirrorPath(diffEntry.oldPath)
        val newFile = mirrorPath(diffEntry.newPath)

        FileUtils.copyFile(oldFile, newFile)
        FileUtils.forceDelete(oldFile)

        mirrorRepository.addToIndex(oldFile.path)
        mirrorRepository.addToIndex(newFile.path)
    }

    private fun standardPath(path: String) = File("$standardDir/$path")

    private fun mirrorPath(path: String) = File("${mirrorRepository.repositoryPath.path}/$path")
}