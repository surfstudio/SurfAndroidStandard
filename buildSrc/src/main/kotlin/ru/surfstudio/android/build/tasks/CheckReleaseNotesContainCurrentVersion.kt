package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.ReleaseNotes
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesNotContainVersionException
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo
import ru.surfstudio.android.build.utils.getPropertyComponent
import ru.surfstudio.android.build.Components


/**
 * Check ReleaseNotes contain current project version
 */
open class CheckReleaseNotesContainCurrentVersion : DefaultTask() {

    @TaskAction
    fun check() {

        Components.value.forEach { component ->

            val releaseNotes: ReleaseNotesInfo = ReleaseNotes.findByComponentName(component.name)

            val version = releaseNotes.versions
                    .find { it.version == component.projectVersion }
                    ?: throw ReleaseNotesNotContainVersionException(releaseNotes, component.projectVersion)

            if (version.isEmpty) throw ReleaseNotesNotContainVersionException(releaseNotes, component.projectVersion)
        }
    }
}