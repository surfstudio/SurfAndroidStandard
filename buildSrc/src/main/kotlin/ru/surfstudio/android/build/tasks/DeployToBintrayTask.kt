package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.artifactory.Artifactory

/**
 * Test task to deploy
 */
open class DeployToBintrayTask : DefaultTask() {

    @TaskAction
    fun deploy() {
        Components.libraries.forEach {
            Artifactory.distributeArtifactToBintray(false, it.name)
        }
    }
}