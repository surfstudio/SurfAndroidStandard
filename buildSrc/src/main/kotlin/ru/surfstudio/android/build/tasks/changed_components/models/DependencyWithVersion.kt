package ru.surfstudio.android.build.tasks.changed_components.models

/**
 * Represent information about dependency with version
 */
data class DependencyWithVersion(
        val name: String,
        var version: String
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DependencyWithVersion) return false
        return other.version == this.version
    }
}