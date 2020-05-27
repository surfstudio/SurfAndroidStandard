package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException

/**
 * Parse release notes md files
 */
class ReleaseNotesParsingException(
        fileName: String,
        cause: ReleaseNotesFormatException
) : GradleException(
        "Can't parse $fileName  file.",
        cause
)