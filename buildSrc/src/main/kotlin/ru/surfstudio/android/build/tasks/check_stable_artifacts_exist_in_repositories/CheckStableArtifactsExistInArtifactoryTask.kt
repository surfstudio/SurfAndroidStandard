package ru.surfstudio.android.build.tasks.check_stable_artifacts_exist_in_repositories

import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.exceptions.artifactory.ArtifactsNotFoundInArtifactoryException
import ru.surfstudio.android.build.model.module.Library

/**
 * Check stable artifacts exist in artifactory
 */
open class CheckStableArtifactsExistInArtifactoryTask : BaseCheckStableArtifactsExistInRepositoryTask() {

    @TaskAction
    fun check() {
        val libraries = findStableLibraries()
        val libsWithNotExistedArtifacts = ArrayList<Library>()

        libraries.forEach { library ->
            if (!Artifactory.isArtifactExists(library.name, library.projectVersion)) {
                libsWithNotExistedArtifacts.add(library)
            }
        }

        if (libsWithNotExistedArtifacts.isNotEmpty()) throw ArtifactsNotFoundInArtifactoryException(libsWithNotExistedArtifacts)
    }
}