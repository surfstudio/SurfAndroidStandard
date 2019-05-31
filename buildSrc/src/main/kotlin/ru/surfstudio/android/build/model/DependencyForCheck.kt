package ru.surfstudio.android.build.model

/**
 * Represent information about dependency with version
 */
data class DependencyForCheck(
        val name: String,
        var version: String
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DependencyForCheck) return false
        return other.version == this.version
    }
}