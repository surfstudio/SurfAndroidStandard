package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesFileNotExistException
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesFormatException
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesParsingException
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * Class encapsulate work with ReleaseNotesInfo
 */
object ReleaseNotes {

    private const val RELEASE_NOTES_FILE_NAME = "RELEASE_NOTES.md"
    private lateinit var releaseNotes: List<ReleaseNotesInfo>
    private val parser = ReleaseNotesParser()

    @JvmStatic
    fun test() {
        parseReleaseNotesFiles()
    }

    /**
     * Parse releaseNotes.md files
     */
    private fun parseReleaseNotesFiles() {
        val releaseNotesFiles = Components.value.map { File("${it.directory}/$RELEASE_NOTES_FILE_NAME") }

        releaseNotesFiles.forEach(this::checkReleaseNotesFilesExist)

        releaseNotes = releaseNotesFiles
                .map { it to BufferedReader(FileReader(it)).readText() }
                .map {
                    val (file, content) = it
                    try {
                        parser.createReleaseNotes(content)
                    } catch (e: ReleaseNotesFormatException) {
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