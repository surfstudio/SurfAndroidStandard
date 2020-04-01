package ru.surfstudio.android.build.exceptions.library

import org.gradle.api.GradleException

/**
 * Library directory doesn't exist
 */
class LibraryDirectoryNotExistException(
        componentId: String,
        libraryName: String,
        libraryDirectory: String
) : GradleException(
        "Component $componentId with library $libraryName doesn't have existing directory $libraryDirectory. " +
                "Please, check value.json and create folder with 'dir' name."
)