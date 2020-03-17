package ru.surfstudio.android.build.tasks.check_release_notes

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.ReleaseNotes
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesNotContainVersionException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo

/**
 * Check ReleaseNotes contain current project version
 */
open class CheckReleaseNotesContainCurrentVersion : DefaultTask() {

    @TaskAction
    fun check() {

        Components.value.forEach { component ->
            val releaseNotes: ReleaseNotesInfo = ReleaseNotes.findByComponentName(component.name)

            val version = releaseNotes.versions
                    .find { it.version.trim() == component.noCounterVersion }
                    ?: throw ReleaseNotesNotContainVersionException(releaseNotes, component.noCounterVersion)

            if (version.isEmpty) {
                throw ReleaseNotesNotContainVersionException(releaseNotes, component.noCounterVersion)
            }
        }
    }

    private val Component.noCounterVersion get() = if (stable) baseVersion else "$baseVersion-alpha"
}