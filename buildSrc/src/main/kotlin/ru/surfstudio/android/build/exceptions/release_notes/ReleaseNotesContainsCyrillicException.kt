package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo

class ReleaseNotesContainsCyrillicException(
        releaseNotes: ReleaseNotesInfo
) : GradleException(
        "Release notes \"${releaseNotes.title}\" contains cyrillic symbols."
)