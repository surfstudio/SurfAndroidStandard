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
     * Publish type
     */
    const val PUBLISH_TYPE = "publishType"

    /**
     * Deploy only unstable components
     */
    const val DEPLOY_ONLY_UNSTABLE_COMPONENTS = "onlyUnstable"

    /**
     * Deploy only is not exist
     */
    const val DEPLOY_ONLY_IF_NOT_EXIST = "deployOnlyIfNotExist"
}