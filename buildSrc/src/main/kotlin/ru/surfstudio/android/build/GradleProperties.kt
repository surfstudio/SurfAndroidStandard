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
     * If artifact exist in artifactory replace it
     */
    const val DEPLOY_SAME_VERSION_ARTIFACTORY = "deploySameVersionArtifactory"

    /**
     * If artifact exist in bintray replace it
     */
    const val DEPLOY_SAME_VERSION_BINTRAY = "deploySameVersionBintray"
}