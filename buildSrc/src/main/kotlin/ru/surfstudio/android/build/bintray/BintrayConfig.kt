package ru.surfstudio.android.build.bintray

import ru.surfstudio.android.build.utils.EMPTY_STRING


/**
 * Class containes bintray's credentials, URLs, repository names, ...
 */
object BintrayConfig {

    private const val BINTRAY_URL = "https://api.bintray.com"
    private const val PACKAGES_URL = "$BINTRAY_URL/packages"
    const val GET_VERSION_URL = "$PACKAGES_URL/surf/maven"

    val USER_NAME: String = "trushchinskii" //TODO System.getenv("surf_bintray_username") ?: EMPTY_STRING
    val PASSWORD: String = "707ba3f9e1161180886cc38dc5ab88279a06322e" //TODO System.getenv("surf_bintray_api_key") ?: EMPTY_STRING
}