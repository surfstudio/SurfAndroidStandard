package ru.surfstudio.android.build.artifactory

import org.gradle.api.GradleException
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.exceptions.ArtifactNotExistInArtifactoryException
import ru.surfstudio.android.build.exceptions.FolderNotFoundException
import ru.surfstudio.android.build.model.ArtifactInfo
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library

/**
 * Provide Artifactory functions
 */
object Artifactory {

    private val repository = ArtifactoryRepository()

    /**
     * Deploy artifact to bintray
     */
    fun distributeArtifactToBintray(overrideExisting: Boolean, vararg libraryNames: String) {
        val artifacts: List<ArtifactInfo> = libraryNames.map { ArtifactInfo(it) }

        var packagesRepoPaths = ""
        artifacts.forEachIndexed { index, artifactInfo ->
            packagesRepoPaths += artifactInfo.getPath()
            if (index != artifacts.size - 1) packagesRepoPaths += ", "
        }

        val response = repository.distribute(packagesRepoPaths, overrideExisting)

        if (!response.body().isEmpty()) throw GradleException(response.toString())
    }

    /**
     * Check libraries's android standard dependencies exist in artifactory
     */
    fun checkLibrariesStandardDependenciesExisting(component: Component) {
        component.libraries.forEach(this::checkLibraryStandardDependenciesExisting)
    }

    /**
     * Check library android standard dependencies exist in artifactory
     */
    private fun checkLibraryStandardDependenciesExisting(library: Library) {
        library.androidStandardDependencies.forEach { androidStandardDependency ->
            if (!isArtifactExists(androidStandardDependency.name, androidStandardDependency.component.projectVersion)) {
                throw ArtifactNotExistInArtifactoryException(library.name, androidStandardDependency)
            }
            Components.libraries.find { it.name == androidStandardDependency.name }?.let {
                checkLibraryStandardDependenciesExisting(it)
            }
        }
    }

    /**
     * Check artifact exist in artifactory
     */
    fun isArtifactExists(dependencyName: String, version: String): Boolean {
        val folderPath = "$dependencyName/$version"
        return try {
            !repository.getFolderInfo(folderPath).isEmpty
        } catch (e: FolderNotFoundException) {
            false
        }
    }
}