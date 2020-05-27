package ru.surfstudio.android.build.tasks.check_stable_artifacts_exist_in_repositories

import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.bintray.Bintray
import ru.surfstudio.android.build.exceptions.bintray.ArtifactsNotFoundInBintrayException
import ru.surfstudio.android.build.model.module.Library

/**
 * Check stable artifacts exist in bintray
 */
open class CheckStableArtifactsExistInBintrayTask : BaseCheckStableArtifactsExistInRepositoryTask() {

    @TaskAction
    fun check() {
        val libraries = findStableLibraries()
        val libsWithNotExistedArtifacts = ArrayList<Library>()

        libraries.forEach { library ->
            if (!Bintray.isArtifactExists(library.name, library.projectVersion)) {
                libsWithNotExistedArtifacts.add(library)
            }
        }

        if (libsWithNotExistedArtifacts.isNotEmpty()) throw ArtifactsNotFoundInBintrayException(libsWithNotExistedArtifacts)
    }
}