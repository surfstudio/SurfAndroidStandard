package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.artifactory.Artifactory

/**
 * Check artifact for android standard dependencies exist in artifactory
 */
open class CheckExistingDependencyArtifactsInArtifactory: DefaultTask() {

    @TaskAction
    fun check(){
        Artifactory.checkLibrariesStandardDependenciesExisting()
    }
}