package ru.surfstudio.android.build.tasks.bintray_tasks.release

/**
 * Check if all release artifacts have a tag with format ARTIFACT-NAME/STABLE-VERSION
 */
open class CheckTagsForReleaseArtifactsTask : BaseCheckBintrayForReleaseTask() {

    override fun performCheck(
            packageName: String,
            stableVersion: String,
            isCheckoutTagForArtifact: Boolean
    ) {
        // the artifact's release tag is not found, the common tag will be used
        if (!isCheckoutTagForArtifact) {
            sb.append(
                    "WARNING! Released artifact $packageName must have " +
                            "a release tag \"$packageName/$stableVersion\"\n"
            )
        }
    }
}