package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException

/**
 * ReleaseNotes doesn't exist in filesystem
 */
class ReleaseNotesFileNotExistException(
        fileName: String
) : GradleException(
        "Release notes file \"$fileName\" doesn't exist"
)