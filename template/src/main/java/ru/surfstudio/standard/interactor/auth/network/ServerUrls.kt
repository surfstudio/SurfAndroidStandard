package ru.surfstudio.standard.interactor.auth.network


/**
 * url запросов сервера
 */

const val PRODUCTION_API_URL = "https://api.golamago.online"
const val TEST_API_URL = "http://lama.api.surfstudio.ru:11114/"
const val AUTH_API_URL = "https://id.golamago.online"

const val LOGIN_PATH = "/connect/token"
const val GET_TOKEN_URL = AUTH_API_URL + LOGIN_PATH

const val SEND_PHONE_PATH = PRODUCTION_API_URL + "/users"
const val GRANT_TYPE_AUTH = "authorization_code"
const val GRANT_TYPE_REFRESH = "refresh_token"
const val CLIENT_ID = "lama.client.android"
const val REDIRECT_URI = "https://golamago.com/callback"

const val CODE_FORMAT = "%s:%s"
