package ru.surfstudio.android.build.maven

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Class contains maven's credentials, URLs, repository names, ...
 */
object MavenConfig {

    const val RELEASE_DEPLOY_URL = "https://oss.sonatype.org/service/local/staging/deploy/maven2"

    val USER_NAME: String = System.getenv("surf_ossrh_username") ?: EMPTY_STRING
    val PASSWORD: String = System.getenv("surf_ossrh_password") ?: EMPTY_STRING
}