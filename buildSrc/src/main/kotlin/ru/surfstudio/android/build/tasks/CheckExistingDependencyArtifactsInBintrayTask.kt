package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.bintray.Bintray

/**
 * Check artifact for android standard dependencies exist in bintray
 */
open class CheckExistingDependencyArtifactsInBintrayTask : DefaultTask() {

    @TaskAction
    fun check() {
        Bintray.checkLibrariesStandardDependenciesExisting()
    }
}