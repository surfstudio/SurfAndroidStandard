package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException

class ReleaseNotesForConfigurationException(
        componentName: String,
        reason: String
) : GradleException(
        "Component $componentName changed its configuration, but its file release notes file was not changed. Because $reason"
)