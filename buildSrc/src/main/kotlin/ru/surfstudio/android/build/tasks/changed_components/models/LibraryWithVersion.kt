package ru.surfstudio.android.build.tasks.changed_components.models

import ru.surfstudio.android.build.model.module.Library
import java.util.*

/**
 * Represent information about library with version
 */
data class LibraryWithVersion(
        val name: String,
        val directory: String,
        var thirdPartyDependencies: List<DependencyWithVersion> = listOf(),
        var androidStandardDependencies: List<DependencyWithVersion> = listOf()
) {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is LibraryWithVersion) return false
        return other.name == this.name
    }

    override fun hashCode() = Objects.hash(name)

    companion object {

        fun create(lib: Library) = LibraryWithVersion(lib.name, lib.directoryPath)
    }
}