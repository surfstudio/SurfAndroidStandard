package ru.surfstudio.android.build.tasks.changed_components

import ru.surfstudio.android.build.tasks.currentDirectory
import java.io.File

/**
 * Creates second project with revision[revisionToCompare] in temp folder [tempFolderName]
 */
class SecondProjectCreator(
        private val revisionToCompare: String,
        private val tempFolderName: String
) {
    private val tempDir = "$currentDirectory/$tempFolderName"

    /**
     * creates copy of current project in  [tempDir] and checks out for that project [revisionToCompare]
     */
    fun createProjectWithRevToCompare() {
        createTempFolder()
        copyProjectToTempFolder()
        checkoutGitRevision()
    }

    /**
     * copies recursively current project to [tempDir] excepting [tempFolderName] in current directory
     */
    private fun copyProjectToTempFolder() {
        val fileFrom = File(currentDirectory)
        fileFrom.listFiles().forEach { file ->
            if (file.name != tempFolderName) {
                file.copyRecursively(File("$tempDir/${file.name}"), true)
            }
        }
    }

    /**
     * creates [tempDir]
     */
    private fun createTempFolder() {
        val tempDirFile = File(tempDir)
        if (tempDirFile.exists()) {
            tempDirFile.deleteRecursively()
        }
        tempDirFile.mkdir()
    }

    /**
     * checks out with git revision with hash [revisionToCompare]
     */
    private fun checkoutGitRevision() {
        GitCommandRunner(tempDir).checkoutRevision(revisionToCompare)
    }
}