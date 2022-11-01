package ru.surfstudio.android.build.utils

const val EMPTY_STRING = ""
const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

/**
 * Create composite version
 *
 * There are 2 types of version:
 * X.Y.Z-unstableVersion - projectSnapshotName is empty
 * X.Y.Z-projectSnapshotName.projectSnapshotVersion - projectSnapshotName isn't empty
 */
fun createCompositeVersion(
        version: String,
        unstableVersion: Int,
        projectSnapshotName: String,
        projectSnapshotVersion: Int
): String {
    return if (projectSnapshotName.isNotEmpty()) {
        "$version-$projectSnapshotName.$projectSnapshotVersion"
    } else {
        "$version-$unstableVersion"
    }
}