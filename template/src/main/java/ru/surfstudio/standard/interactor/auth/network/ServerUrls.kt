package ru.surfstudio.standard.interactor.auth.network


/**
 * url запросов сервера
 */

//todo определить проектные url и path

const val PRODUCTION_API_URL = "https://api"
const val TEST_API_URL = "http://test.api"

const val USER_LOGOUT_PATH = "/logout"

const val GET_TOKEN_PATH = "/connect/token"

const val LOGIN_BY_PHONE_PATH = "/login/phone"

const val LOGIN_BY_EMAIL_PATH = "/login/email"

const val RECOVER_ACCESS_BY_EMAIL_PATH = "recover/email"

const val RECOVER_ACCESS_BY_PHONE_PATH = "recover/phone"
