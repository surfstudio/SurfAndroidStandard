package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo

class ReleaseNotesNotContainVersionException(
        releaseNotes: ReleaseNotesInfo,
        version: String
) : GradleException(
        "Release notes \"${releaseNotes.title}\" doesn't contain version \"$version\""
)