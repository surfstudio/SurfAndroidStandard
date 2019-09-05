package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException

class ReleaseNotesNotFoundException(componentName: String) : GradleException(
        "ReleaseNotes not found for component \"$componentName\""
)