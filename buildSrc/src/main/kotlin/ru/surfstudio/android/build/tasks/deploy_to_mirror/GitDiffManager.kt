package ru.surfstudio.android.build.tasks.deploy_to_mirror

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.diff.DiffEntry
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.MirrorRepository
import java.io.File
import java.io.FileNotFoundException

/**
 * Work with git changes
 */
class GitDiffManager(
        private val standardDir: String,
        private val mirrorRepository: MirrorRepository
) {

    /**
     * work when file was added. Copy new standard file and add to index
     *
     * @param diffEntry information about diff
     */
    fun add(diffEntry: DiffEntry) {
        val standardFile = standardPath(diffEntry.newPath)
        val mirrorFile = mirrorPath(diffEntry.newPath)

        FileUtils.copyFile(standardFile, mirrorFile)

        mirrorRepository.addToIndex(mirrorFile.path)
    }

    /**
     * work when file was moved from one place to another. Copy file and add to index
     *
     * @param diffEntry information about diff
     */
    fun copy(diffEntry: DiffEntry) {
        val copyFrom = mirrorPath(diffEntry.oldPath)
        val copyTo = mirrorPath(diffEntry.newPath)

        FileUtils.copyFile(copyFrom, copyTo)

        mirrorRepository.addToIndex(copyTo.path)
    }

    /**
     * work when file was deleted. Delete file and add to index
     *
     * @param diffEntry information about diff
     */
    fun delete(diffEntry: DiffEntry) {
        try {
            val file = mirrorPath(diffEntry.oldPath)

            FileUtils.forceDelete(file)

            mirrorRepository.addToIndex(file.path)
        } catch (ignored: FileNotFoundException) {

        }
    }

    /**
     * work when file was modified
     *
     * @param diffEntry information about diff
     */
    fun modify(diffEntry: DiffEntry) = modify(diffEntry.oldPath)


    /**
     * work when file was modified. Copies file from standard to mirror one
     *
     * @param filePath information about diff
     */
    fun modify(filePath: String) {
        val standardFile = standardPath(filePath)
        val mirrorFile = mirrorPath(filePath)

        FileUtils.copyFile(standardFile, mirrorFile)

        mirrorRepository.addToIndex(filePath)
    }

    /**
     * work when file was renamed. Copy old file to new file name and remove old filename
     *
     * @param diffEntry information about diff
     */
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