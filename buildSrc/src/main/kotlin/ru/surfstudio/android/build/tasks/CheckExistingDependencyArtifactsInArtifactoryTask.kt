package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.exceptions.ComponentNotFoundException

/**
 * Check artifact for android standard dependencies exist in artifactory
 */
open class CheckExistingDependencyArtifactsInArtifactoryTask : DefaultTask() {

    @TaskAction
    fun check() {
        val componentName = project.property(GradleProperties.COMPONENT) as? String
                ?: throw ComponentPropertyNotFoundException()
        val component = Components.value.find { it.name == componentName }
                ?: throw ComponentNotFoundException()

        Artifactory.checkLibrariesStandardDependenciesExisting(component)
    }
}