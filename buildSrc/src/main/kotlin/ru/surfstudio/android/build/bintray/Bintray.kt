package ru.surfstudio.android.build.bintray

import ru.surfstudio.android.build.exceptions.ArtifactNotExistInBintrayException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library

/**
 * Provide Bintray functions
 */
object Bintray {

    private val repository = BintrayRepository()

    /**
     * Check libraries's android standard dependencies exist in bintray
     */
    fun checkLibrariesStandardDependenciesExisting(component: Component) {
        component.libraries.forEach(this::checkLibraryStandardDependenciesExisting)
    }

    /**
     * Check library's android standard dependencies exist in bintray
     */
    private fun checkLibraryStandardDependenciesExisting(library: Library) {
        library.androidStandardDependencies.forEach { androidStandardDependency ->
            if (!repository.isArtifactVersionExist(
                            androidStandardDependency.name,
                            androidStandardDependency.component.projectVersion)
            ) {
                throw ArtifactNotExistInBintrayException(library.name, androidStandardDependency)
            }
        }

    }
}