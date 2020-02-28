package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Parsing folderInfo Exception
 */
class FolderInfoParsingException(data: String) : GradleException(
        "Exception when getFolderInfo folderPath. Can't parse $data"
)