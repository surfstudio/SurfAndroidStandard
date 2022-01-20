package ru.surfstudio.android.build.bintray

import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.model.BintrayRepoLatestVersion
import ru.surfstudio.android.build.exceptions.bintray.ArtifactNotExistInBintrayException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library

/**
 * Provide Bintray functions
 */
@Deprecated("Use Artifactory or Maven Central after Bintray sunset")
object Bintray {

    private val repository = BintrayRepository()

    /**
     * Check if libraries's android standard dependencies exist in bintray
     */
    fun checkLibrariesStandardDependenciesExisting(component: Component) {
        component.libraries.forEach { this.checkLibraryStandardDependenciesExisting(it, component) }
    }

    /**
     * Check if library's android standard dependencies exist in bintray
     */
    private fun checkLibraryStandardDependenciesExisting(library: Library, component: Component) {
        library.androidStandardDependencies
                .filter { it.component != component }
                .forEach { androidStandardDependency ->
                    if (!isArtifactExists(
                                    androidStandardDependency.name,
                                    androidStandardDependency.component.projectVersion
                            )
                    ) {
                        throw ArtifactNotExistInBintrayException(library.name, androidStandardDependency)
                    }

                    Components.libraries.find { it.name == androidStandardDependency.name }?.let {
                        checkLibraryStandardDependenciesExisting(it, component)
                    }
                }
    }

    /**
     * Check if artifact exists in bintray
     */
    fun isArtifactExists(dependencyName: String, version: String): Boolean =
            repository.isArtifactVersionExist(dependencyName, version)

    /**
     * Function for getting all packages from bintray
     */
    fun getAllPackages(): List<String> =
            repository.getAllPackages()

    /**
     * Function for getting the latest version of artifact in bintray
     */
    fun getArtifactLatestVersion(artifactName: String): BintrayRepoLatestVersion =
            repository.getArtifactLatestVersion(artifactName)
}