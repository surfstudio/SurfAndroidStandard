package ru.surfstudio.android.build

/**
 * Gradle properties
 */
object GradleProperties {

    /**
     * Component
     */
    const val COMPONENT = "component"

    /**
     * Components list for check standard dependency stability
     */
    const val COMPONENTS_TO_CHECK_STANDARD_DEPENDENCIES_STABILITY =
            "componentsToCheckStandardDependenciesStability"

    /**
     * If artifact exist in artifactory replace it
     */
    const val DEPLOY_SAME_VERSION_ARTIFACTORY = "deploySameVersionArtifactory"

    /**
     * If artifact exist in bintray replace it
     */
    const val DEPLOY_SAME_VERSION_BINTRAY = "deploySameVersionBintray"
}