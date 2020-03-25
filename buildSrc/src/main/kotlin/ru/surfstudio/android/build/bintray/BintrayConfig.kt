package ru.surfstudio.android.build.bintray

import ru.surfstudio.android.build.utils.EMPTY_STRING


/**
 * Class containes bintray's credentials, URLs, repository names, ...
 */
object BintrayConfig {

    private const val BINTRAY_URL = "https://api.bintray.com"
    private const val PACKAGES_URL = "$BINTRAY_URL/packages"
    private const val SURF_BINTRAY_URL = "surf/maven"
    const val GET_VERSION_URL = "$PACKAGES_URL/$SURF_BINTRAY_URL"
    const val GET_ALL_PACKAGES_URL = "$BINTRAY_URL/repos/$SURF_BINTRAY_URL/packages"

    val USER_NAME: String = System.getenv("surf_bintray_username") ?: EMPTY_STRING
    val PASSWORD: String = System.getenv("surf_bintray_api_key") ?: EMPTY_STRING
}