package ru.surfstudio.android.build.artifactory

import com.github.kittinunf.fuel.core.isSuccessful
import org.gradle.api.GradleException
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.exceptions.ArtifactNotExistInArtifactoryException
import ru.surfstudio.android.build.exceptions.FolderNotFoundException
import ru.surfstudio.android.build.model.ArtifactInfo
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.dependency.AndroidStandardDependency
import ru.surfstudio.android.build.model.module.Library

/**
 * Provide Artifactory functions
 */
object Artifactory {

    private val repository = ArtifactoryRepository()

    /**
     * Deploy artifact to bintray
     */
    fun distributeArtifactToBintray(vararg libraryNames: String) {
        val artifacts: List<ArtifactInfo> = libraryNames.map { ArtifactInfo(it) }

        var packagesRepoPaths = ""
        artifacts.forEachIndexed { index, artifactInfo ->
            packagesRepoPaths += artifactInfo.getPath()
            if (index != artifacts.size - 1) packagesRepoPaths += ", "
        }

        val response = repository.distribute(packagesRepoPaths)
        if (!response.isSuccessful) throw GradleException(response.toString())
    }

    /**
     * Check libraries's android standard dependencies exist in artifactory
     */
    fun checkLibrariesStandardDependenciesExisting() {
        Components.value
                .flatMap(Component::libraries)
                .forEach(this::checkLibraryStandardDependenciesExisting)
    }

    /**
     * Check library's android standard dependencies exist in artifactory
     */
    private fun checkLibraryStandardDependenciesExisting(library: Library) {
        library.androidStandardDependencies.forEach { androidStandardDependency ->
            if (!isStandardDependenciesExist(androidStandardDependency)) {
                throw ArtifactNotExistInArtifactoryException(library.name, androidStandardDependency)
            }
        }

    }

    private fun isStandardDependenciesExist(androidStandardDependency: AndroidStandardDependency): Boolean {
        val folderPath = "${androidStandardDependency.name}/${androidStandardDependency.component.projectVersion}"
        return try {
            !repository.getFolderInfo(folderPath).isEmpty
        } catch (e: FolderNotFoundException) {
            false
        }

    }
}