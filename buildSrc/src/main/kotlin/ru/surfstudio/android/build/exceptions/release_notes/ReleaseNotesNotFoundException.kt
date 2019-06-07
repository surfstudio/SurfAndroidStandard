package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.Component

class ReleaseNotesNotFoundException(component: Component) : GradleException(
        "ReleaseNotes not found for component \"${component.name}\""
)