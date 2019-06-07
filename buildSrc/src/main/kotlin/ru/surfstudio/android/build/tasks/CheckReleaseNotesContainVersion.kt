package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.ReleaseNotes

open class CheckReleaseNotesContainVersion : DefaultTask() {

    @TaskAction
    fun check() {
        //TODO возможно надо переделать на один компонент
        Components.value.forEach { component ->
            println(ReleaseNotes.value)
        }
    }
}