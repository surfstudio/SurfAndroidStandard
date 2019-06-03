package ru.surfstudio.android.build.tasks.changed_components.models

import ru.surfstudio.android.build.model.Library

/**
 * Represent information about library with version
 */
data class LibraryWithVersion(
        val name: String,
        val directory: String,
        var thirdPartyDependencies: List<DependencyWithVersion> = listOf(),
        var androidStandardDependencies: List<DependencyWithVersion> = listOf()
) {
    companion object {

        fun create(lib: Library)
                : LibraryWithVersion {
            return LibraryWithVersion(lib.name, lib.directory)
        }
    }
}