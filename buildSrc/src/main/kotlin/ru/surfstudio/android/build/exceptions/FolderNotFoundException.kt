package ru.surfstudio.android.build.exceptions

import org.gradle.api.GradleException

/**
 * Throw when folder doesn't exist somewhere
 */
class FolderNotFoundException(folderPath: String) : GradleException(
        "Can't find folder with folderPath : $folderPath in artifactory."
)