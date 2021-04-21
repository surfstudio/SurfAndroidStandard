package ru.surfstudio.android.build.maven

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Class contains maven's credentials, URLs, repository names, ...
 */
object MavenConfig {

    val SIGN_KEY_ID: String = System.getenv("surf_maven_sign_key_id") ?: EMPTY_STRING
    val SIGN_PASSWORD: String = System.getenv("surf_maven_sign_password") ?: EMPTY_STRING
    val SIGN_KEY_RING_FILE: String = System.getenv("surf_maven_sign_key_ring_file") ?: EMPTY_STRING

    val USER_NAME: String = System.getenv("surf_ossrh_username") ?: EMPTY_STRING
    val PASSWORD: String = System.getenv("surf_ossrh_password") ?: EMPTY_STRING
}