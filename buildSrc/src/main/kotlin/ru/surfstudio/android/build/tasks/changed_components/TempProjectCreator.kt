package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.currentDirectory
import java.io.File

/**
 * Creates temp project with revision[revisionToCompare] in temp directory [tempFolderName]
 */
class TempProjectCreator(
        private val revisionToCompare: String,
        private val tempFolderName: String
) {

    private val tempDirectory = "$currentDirectory/$tempFolderName"

    /**
     * creates copy of current project in  [tempDirectory] and checks out for that project [revisionToCompare]
     */
    fun createProjectWithRevToCompare() {
        createTempFolder()
        copyProjectToTempFolder()
        checkoutGitRevision()
    }

    /**
     * copies recursively current project to [tempDirectory] excepting [tempFolderName] in current directoryPath
     */
    private fun copyProjectToTempFolder() {
        val fileFrom = File(currentDirectory)
        fileFrom.listFiles().forEach { file ->
            if (file.name != tempFolderName) {
                file.copyRecursively(File("$tempDirectory/${file.name}"), true)
            }
        }
    }

    /**
     * creates [tempDirectory]
     */
    private fun createTempFolder() {
        val tempDirFile = File(tempDirectory)
        if (tempDirFile.exists()) {
            tempDirFile.deleteRecursively()
        }
        tempDirFile.mkdir()
    }

    /**
     * checks out with git revision with hash [revisionToCompare]
     */
    private fun checkoutGitRevision() {
        GitCommandRunner(tempDirectory).checkoutRevision(revisionToCompare)
    }
}