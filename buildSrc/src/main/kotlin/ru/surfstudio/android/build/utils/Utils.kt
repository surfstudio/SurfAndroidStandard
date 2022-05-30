package ru.surfstudio.android.build.utils

const val EMPTY_STRING = ""
const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

/**
 * Create composite version
 *
 * There are 2 types of version:
 * X.Y.Z-unstable_version - projectSnapshotName is empty
 * X.Y.Z-unstable_version.projectSnapshotName.projectVersion - projectSnapshotName isn't empty
 */
fun createCompositeVersion(
        version: String,
        unstableVersion: Int,
        projectSnapshotName: String,
        projectSnapshotVersion: Int
): String {
    var compositeVersion = "$version-$unstableVersion"
    if (projectSnapshotName.isNotEmpty()) {
        compositeVersion += "-$projectSnapshotName.$projectSnapshotVersion"
    }
    return compositeVersion
}