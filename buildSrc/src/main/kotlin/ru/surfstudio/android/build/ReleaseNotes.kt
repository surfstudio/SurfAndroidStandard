package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesFileNotExistException
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesFormatException
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesNotFoundException
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesParsingException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

const val RELEASE_NOTES_FILE_NAME = "RELEASE_NOTES.md"

/**
 * Class encapsulate work with ReleaseNotesInfo
 */
object ReleaseNotes {

    private val parser = ReleaseNotesParser()

    val values: List<ReleaseNotesInfo> by lazy { parseReleaseNotesFiles() }

    fun findByComponentName(componentName: String): ReleaseNotesInfo {
        return values.find { it.component.name == componentName }
                ?: throw ReleaseNotesNotFoundException(componentName)
    }

    fun getReleaseNotesFilePath(component: Component): String =
            "${component.directory}/$RELEASE_NOTES_FILE_NAME"

    /**
     * Parse releaseNotes.md files
     */
    private fun parseReleaseNotesFiles(): List<ReleaseNotesInfo> {
        return Components.value.map { component ->
            val file = File(getReleaseNotesFilePath(component))

            checkReleaseNotesFilesExist(file)

            val content = BufferedReader(FileReader(file)).readText()

            try {
                parser.createReleaseNotes(component, content)
            } catch (e: ReleaseNotesFormatException) {
                e.printStackTrace()
                throw ReleaseNotesParsingException(file.path, e)
            }
        }
    }

    /**
     * Check release notes file exists
     */
    private fun checkReleaseNotesFilesExist(releaseNotesFile: File) {
        if (!releaseNotesFile.exists()) ReleaseNotesFileNotExistException(releaseNotesFile.name)
    }
}