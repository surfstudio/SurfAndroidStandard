package ru.surfstudio.android.build.maven

/**
 * Class contains maven's credentials, URLs, repository names, ...
 */
object MavenConfig {
    const val RELEASE_DEPLOY_URL = "https://oss.sonatype.org/service/local/staging/deploy/maven2"

    const val USERNAME_ENV_NAME = "surf_ossrh_username"
    const val PASSWORD_ENV_NAME = "surf_ossrh_password"
}