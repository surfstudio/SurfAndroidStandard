package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.bintray.Bintray
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check artifact for android standard dependencies exist in bintray
 */
open class CheckExistingDependencyArtifactsInBintrayTask : DefaultTask() {

    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()

        Bintray.checkLibrariesStandardDependenciesExisting(component)
    }
}