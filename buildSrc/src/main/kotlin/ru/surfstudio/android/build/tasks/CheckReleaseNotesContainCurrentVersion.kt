package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.ReleaseNotes
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesNotContainVersionException
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesNotFoundException
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check ReleaseNotes contain current project version
 */
open class CheckReleaseNotesContainCurrentVersion : DefaultTask() {

    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()

        val releaseNotes: ReleaseNotesInfo = ReleaseNotes.value.find { it.component.name == component.name }
                ?: throw ReleaseNotesNotFoundException(component)

        val version = releaseNotes.versions
                .find { it.version == component.projectVersion }
                ?: throw ReleaseNotesNotContainVersionException(releaseNotes, component.projectVersion)

        if (version.isEmpty) throw ReleaseNotesNotContainVersionException(releaseNotes, component.projectVersion)
    }
}