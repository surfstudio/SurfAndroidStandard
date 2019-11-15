package ru.surfstudio.standard.i_network

import ru.surfstudio.android.template.i_network.BuildConfig

/**
 * URL всех серверных запросов
 */

//todo определить проектные url и path

private const val ACCESS_TOKEN_PATH = "sso/oauth2/access_token"
const val AUTH_ACCESS_TOKEN = ACCESS_TOKEN_PATH

const val BASE_API_URL = BuildConfig.BASE_URL

const val TEST_API_URL = "http://test.api"

const val USER_LOGOUT_PATH = "/logout"

const val GET_TOKEN_PATH = "/connect/token"

const val LOGIN_BY_PHONE_PATH = "/login/phone"