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
     * Path to project to create configuration file information for
     */
    const val CREATE_PROJECT_CONFIGURATION_PATH_TO_PROJECT = "pathToProject"

    /**
     * Revision of project to create configuration file information for
     */
    const val CREATE_PROJECT_CONFIGURATION_REVISION = "revision"

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

    /**
     * Commit in android standard to start mirroring from
     */
    const val COMMIT = "commit"

    /**
     * Mirror dir
     */
    const val MIRROR_DIR = "mirrorDir"

    /**
     * Mirror url
     */
    const val MIRROR_URL = "mirrorUrl"

    /**
     * Depth of standard repository git tree to get commits
     */
    const val DEPLOY_TO_MIRROR_DEPTH_LIMIT = "depthLimit"

    /**
     * Depth of mirror repository tree to search standard commit hash in
     */
    const val DEPLOY_TO_MIRROR_SEARCH_LIMIT = "searchLimit"
}