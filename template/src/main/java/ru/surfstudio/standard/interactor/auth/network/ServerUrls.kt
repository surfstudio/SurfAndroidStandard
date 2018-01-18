package ru.surfstudio.standard.interactor.auth.network


/**
 * url запросов сервера
 */

//todo определить проектные url и path

const val PRODUCTION_API_URL = "https://api"
const val TEST_API_URL = "http://lama.api.surfstudio.ru:11114/"

const val USER_LOGOUT_PATH = "/logout"
const val USER_LOGOUT_URL = PRODUCTION_API_URL + USER_LOGOUT_PATH

const val LOGIN_PATH = "/connect/token"
const val GET_TOKEN_URL = PRODUCTION_API_URL + LOGIN_PATH

const val LOGIN_BY_PHONE_PATH = "/login/phone"
const val LOGIN_BY_PHONE_URL = PRODUCTION_API_URL + LOGIN_BY_PHONE_PATH

const val LOGIN_BY_EMAIL_PATH = "/login/email"
const val LOGIN_BY_EMAIL_URL = PRODUCTION_API_URL + LOGIN_BY_EMAIL_PATH

const val RECOVER_ACCESS_BY_EMAIL_PATH = "recover/email"
const val RECOVER_ACCESS_BY_EMAIL_URL = PRODUCTION_API_URL + RECOVER_ACCESS_BY_EMAIL_PATH

const val RECOVER_ACCESS_BY_PHONE_PATH = "recover/phone"
const val RECOVER_ACCESS_BY_PHONE_URL = PRODUCTION_API_URL + RECOVER_ACCESS_BY_PHONE_PATH
