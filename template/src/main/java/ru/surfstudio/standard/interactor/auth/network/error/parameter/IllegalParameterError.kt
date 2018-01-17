package ru.surfstudio.standard.interactor.auth.network.error.parameter

/**
 * Ошибки входных параметров
 */

enum class IllegalParameterError {
    WRONG_PHONE_NUMBER_FORMAT,
    WRONG_USER_NAME_FORMAT,
    PHONE_NUMBER_NOT_SET,
    SMS_CODE_NOT_SET,
    USER_NAME_NOT_SET
}
