package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.utils.createCompositeVersion

/**
 * Configuration information
 *
 * @param commonVersion - common project version. Format : 0.0.0
 * @param unstableVersion - unique part of artifact version which is incremented for each deploy
 * @param projectSnapshotName - project snapshot name. For project's android standard
 * @param projectSnapshotVersion - project snapshot version
 */
data class GlobalConfigInfo(
        val commonVersion: String,
        val unstableVersion: Int,
        val projectSnapshotName: String,
        val projectSnapshotVersion: Int
) {

    /**
     * Version for android - standard
     */
    val version: String by lazy {
        createCompositeVersion(
                commonVersion,
                unstableVersion,
                projectSnapshotName,
                projectSnapshotVersion
        )
    }
}