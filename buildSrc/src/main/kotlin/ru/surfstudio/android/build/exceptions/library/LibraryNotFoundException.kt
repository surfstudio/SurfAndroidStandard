package ru.surfstudio.android.build.exceptions.library

import org.gradle.api.GradleException

/**
 * Library not found
 */
class LibraryNotFoundException(
        libraryName: String
) : GradleException(
        "Library \"$libraryName\" not found in component.json"
)