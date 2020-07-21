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
     * Components list for check standard dependency stability
     */
    const val COMPONENTS_TO_CHECK_STANDARD_DEPENDENCIES_STABILITY =
            "componentsToCheckStandardDependenciesStability"

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
     * If artifact exist in artifactory replace it
     */
    const val DEPLOY_SAME_VERSION_ARTIFACTORY = "deploySameVersionArtifactory"

    /**
     * If artifact exist in bintray replace it
     */
    const val DEPLOY_SAME_VERSION_BINTRAY = "deploySameVersionBintray"

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

    /**
     * Override existed artifacts
     */
    const val OVERRIDE_EXISTED = "overrideExisted"

    /**
     * Build url
     */
    const val BUILD_URL = "buildUrl"

    /**
     * Is build successful
     */
    const val IS_BUILD_SUCCESSFUL = "isBuildSuccessful"

    /**
     * Is build unstable
     */
    const val IS_BUILD_UNSTABLE = "isBuildUnstable"
}