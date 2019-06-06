package ru.surfstudio.android.build.artifactory

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Class containes artifactory's credentials, URLs, repository names, ...
 */
object ArtifactoryConfig {

    const val ARTIFACTORY_URL = "https://artifactory.surfstudio.ru/artifactory"

    const val TARGET_REPO = "libs-release-remote"
    const val SOURCE_REPO = "libs-release-local"

    const val API_URL = "$ARTIFACTORY_URL/api"
    const val DISTRIBUTE_URL = "$API_URL/distribute"
    const val GET_FOLDER_INFO = "$API_URL/storage/$SOURCE_REPO"

    const val DEPLOY_URL = "$ARTIFACTORY_URL/$SOURCE_REPO"
    const val ANDROID_STANDARD_GROUP_ID = "ru.surfstudio.android"

    val USER_NAME: String = System.getenv("surf_maven_username") ?: EMPTY_STRING
    val PASSWORD: String = System.getenv("surf_maven_password") ?: EMPTY_STRING
}