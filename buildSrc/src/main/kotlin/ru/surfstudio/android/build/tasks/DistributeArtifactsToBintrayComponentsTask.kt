package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Distribute artifacts for component's libraries to bintray
 *
 * It's need to start dependent task :[libname]:distributeArtifactsToBintray
 */
open class DistributeArtifactsToBintrayComponentsTask : DefaultTask() {

    @TaskAction
    fun upload() {

    }
}