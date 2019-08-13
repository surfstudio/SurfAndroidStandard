package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties.OVERRIDE_EXISTED
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.utils.getPropertyComponent
import ru.surfstudio.android.build.utils.readProperty

/**
 * Distribute artifacts to Bintray
 */
open class DistributeArtifactsToBintrayTask : DefaultTask() {

    @TaskAction
    fun distribute() {
        val component = project.getPropertyComponent()
        val overrideExisted = project.readProperty(OVERRIDE_EXISTED, "false").toBoolean()
        Artifactory.distributeArtifactToBintray(overrideExisted, component.libraries.map(Library::name))
    }
}