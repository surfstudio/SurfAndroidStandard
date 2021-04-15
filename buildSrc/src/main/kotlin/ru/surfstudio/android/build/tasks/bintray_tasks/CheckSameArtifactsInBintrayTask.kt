package ru.surfstudio.android.build.tasks.bintray_tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.bintray.Bintray
import ru.surfstudio.android.build.exceptions.bintray.SameArtifactVersionInBintrayException
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check same artifact version in bintray.
 */
@Deprecated("Use Artifactory or Maven Central after Bintray sunset")
open class CheckSameArtifactsInBintrayTask : DefaultTask() {

    /**
     * Check artifact with same version in bintray
     *
     * @throws SameArtifactVersionInBintrayException when artifact with same
     * version exists in bintray
     */
    @TaskAction
    fun check() {
        val allowSameVersion = project.property(GradleProperties.DEPLOY_SAME_VERSION_BINTRAY) as? Boolean
                ?: false

        if (allowSameVersion) return

        val component = project.getPropertyComponent()

        component.libraries.forEach { library ->
            if (Bintray.isArtifactExists(library.name, library.projectVersion)) {
                throw SameArtifactVersionInBintrayException(library.name, library.projectVersion)
            }
        }
    }
}