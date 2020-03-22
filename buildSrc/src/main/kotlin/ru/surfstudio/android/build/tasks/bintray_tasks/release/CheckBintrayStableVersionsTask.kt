package ru.surfstudio.android.build.tasks.bintray_tasks.release

import ru.surfstudio.android.build.bintray.Bintray

/**
 * Check if all artifacts in bintray have stable version as latest.
 */
open class CheckBintrayStableVersionsTask : BaseCheckBintrayForReleaseTask() {

    override fun performCheck(
            packageName: String,
            stableVersion: String,
            isCheckoutTagForArtifact: Boolean
    ) {
        val bintrayVersion = Bintray.getArtifactLatestVersion(packageName).name
        if (stableVersion != bintrayVersion) {
            val errorMessage = "latest bintray version=$bintrayVersion for $packageName " +
                    "is not stable=$stableVersion"
            sb.append("$errorMessage\n")
            println("ERROR: $errorMessage\n")
        } else {
            println("SUCCESS: latest bintray version=$bintrayVersion for $packageName is stable\n")
        }
    }
}