package ru.surfstudio.android.build.utils

const val EMPTY_STRING = ""
const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

/**
 * Create composite version
 *
 * There are 4 types of version:
 * 1. X.Y.Z - isStable = true, projectSnapshotName is empty
 * 2. X.Y.Z-alpha.unstable_version - isStable = false, projectSnapshotName is empty
 * 3. X.Y.Z-projectPostfix.projectVersion - isStable = true, projectSnapshotName isn't empty
 * 4. X.Y.Z-alpha.unstable_version-projectPostfix.projectVersion - isStable = false, projectSnapshotName isn't empty
 */
fun createCompositeVersion(
        version: String,
        isStable: Boolean,
        unstableVersion: Int,
        projectSnapshotName: String,
        projectSnapshotVersion: Int
): String {
    var compositeVersion = version

    if (!isStable) compositeVersion += "-alpha.$unstableVersion"
    if (projectSnapshotName.isNotEmpty()) compositeVersion += "-$projectSnapshotName.$projectSnapshotVersion"

    return compositeVersion
}