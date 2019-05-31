package ru.surfstudio.android.build.model

/**
 * Represent information about project configuration
 */
data class ProjectConfiguration(
        val revision: String,
        val components: List<ComponentForCheck>,
        val libMinSdkVersion: Int,
        val targetSdkVersion: Int,
        val moduleVersionCode: Int,
        val compileSdkVersion: Int
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ProjectConfiguration) return false
        return this.libMinSdkVersion.equals(other.libMinSdkVersion) &&
                this.targetSdkVersion.equals(other.targetSdkVersion) &&
                this.moduleVersionCode.equals(other.moduleVersionCode) &&
                this.compileSdkVersion.equals(other.compileSdkVersion)

    }
}