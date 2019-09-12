package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Upload archives for component's libraries
 *
 * It's need to start dependent task :[libname]:uploadArchives
 */
open class UploadArchiveComponentsTask : DefaultTask() {

    @TaskAction
    fun upload() {

    }
}