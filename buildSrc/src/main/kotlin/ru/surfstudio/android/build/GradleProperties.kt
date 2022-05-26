package ru.surfstudio.android.build

/**
 * Gradle properties
 */
object GradleProperties {

    /**
     * Uses when task needs component
     */
    const val COMPONENT = "component"

    /**
     * Uses when task needs component version
     */
    const val COMPONENT_VERSION = "componentVersion"

    /**
     * Revision to compare with current revision
     */
    const val COMPONENTS_CHANGED_REVISION_TO_COMPARE = "revisionToCompare"

    /**
     * Current revision to compare with previous
     */
    const val CURRENT_REVISION = "currentRevision"

    /**
     * Publish type
     */
    const val PUBLISH_TYPE = "publishType"

    /**
     * If artifact exist in artifactory replace it
     */
    const val DEPLOY_SAME_VERSION_ARTIFACTORY = "deploySameVersionArtifactory"

    /**
     * Deploy only unstable components
     */
    const val DEPLOY_ONLY_UNSTABLE_COMPONENTS = "onlyUnstable"

    /**
     * Deploy only is not exist
     */
    const val DEPLOY_ONLY_IF_NOT_EXIST = "deployOnlyIfNotExist"
}