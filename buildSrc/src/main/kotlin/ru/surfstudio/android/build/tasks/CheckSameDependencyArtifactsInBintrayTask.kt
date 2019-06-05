package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.bintray.Bintray
import ru.surfstudio.android.build.exceptions.ModuleNotFoundInComponentsJsonException
import ru.surfstudio.android.build.exceptions.SameArtifactVersionInBintrayException

/**
 * Check same artifact version in bintray.
 */
open class CheckSameDependencyArtifactsInBintrayTask : DefaultTask() {

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

        val library = Components.libraries.find { it.name == project.name }
                ?: throw ModuleNotFoundInComponentsJsonException(project.name)

        if (Bintray.isArtifactExists(library.name, library.projectVersion)) {
            throw SameArtifactVersionInBintrayException(library.name, library.projectVersion)
        }
    }
}