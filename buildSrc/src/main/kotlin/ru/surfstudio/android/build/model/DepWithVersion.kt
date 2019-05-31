package ru.surfstudio.android.build.model

/**
 * Represent information about dependency with version
 */
data class DepWithVersion(
        val name: String,
        var version: String
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DepWithVersion) return false
        return other.version == this.version
    }
}