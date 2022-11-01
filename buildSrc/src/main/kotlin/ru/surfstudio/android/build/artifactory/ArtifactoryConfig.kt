package ru.surfstudio.android.build.artifactory

/**
 * Class contains artifactory's credentials, URLs, repository names, ...
 */
object ArtifactoryConfig {
    const val DEPLOY_URL = "https://artifactory.surfstudio.ru/artifactory/libs-release-local"
    const val ANDROID_STANDARD_GROUP_ID = "ru.surfstudio.android"

    const val USERNAME_ENV_NAME = "surf_maven_username"
    const val PASSWORD_ENV_NAME = "surf_maven_password"
}