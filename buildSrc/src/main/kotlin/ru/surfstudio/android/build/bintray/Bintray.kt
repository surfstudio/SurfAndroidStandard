package ru.surfstudio.android.build.bintray

import ru.surfstudio.android.build.exceptions.ArtifactNotExistInBintrayException
import ru.surfstudio.android.build.model.Component

/**
 * Provide Bintray functions
 */
object Bintray {

    private val repository = BintrayRepository()

    /**
     * Check libraries's android standard dependencies exist in bintray
     */
    fun checkLibrariesStandardDependenciesExisting(component: Component) {
        component.libraries.forEach { library ->
            library.androidStandardDependencies.forEach { androidStandardDependency ->
                if (!isArtifactExists(
                                androidStandardDependency.name,
                                androidStandardDependency.component.projectVersion)
                ) {
                    throw ArtifactNotExistInBintrayException(library.name, androidStandardDependency)
                }
            }
        }
    }

    /**
     * Check artifact exist in bintray
     */
    fun isArtifactExists(dependencyName: String, version: String): Boolean {
        return repository.isArtifactVersionExist(dependencyName, version)
    }
}