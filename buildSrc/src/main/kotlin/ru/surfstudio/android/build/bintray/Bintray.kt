package ru.surfstudio.android.build.bintray

import ru.surfstudio.android.build.Components
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
            if (!isStandardDependenciesExist(
                            androidStandardDependency.name,
                            androidStandardDependency.component.projectVersion
                    )
            ) {
                throw ArtifactNotExistInBintrayException(library.name, androidStandardDependency)
            }

            Components.libraries.find { it.name == androidStandardDependency.name }?.let {
                checkLibraryStandardDependenciesExisting(it)
            }
        }
    }

    private fun isStandardDependenciesExist(dependencyName: String, version: String): Boolean {
        return repository.isArtifactVersionExist(dependencyName, version)
    }
}