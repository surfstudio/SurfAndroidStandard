package ru.surfstudio.android.build.tasks.bintray_tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties.OVERRIDE_EXISTED
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.utils.getPropertyComponent
import ru.surfstudio.android.build.utils.readProperty

/**
 * Distribute artifacts to Bintray.
 *
 * This task can only be used for deploy of a single artifact via the name.
 * For deploy of multiple artifacts to bintray, see [distributeArtifactsToBintray] gradle task
 */
open class DistributeArtifactsToBintrayTask : DefaultTask() {

    @TaskAction
    fun distribute() {
        val component = project.getPropertyComponent()
        val overrideExisted = project.readProperty(OVERRIDE_EXISTED, "false").toBoolean()
        Artifactory.distributeArtifactToBintray(overrideExisted, component.libraries.map(Library::name))
    }
}