package ru.surfstudio.standard.interactor.auth.network.error.sms

/**
 * Типы ошибок возникающие при отсылке смс сервером
 */

enum class SmsSendingError {
    SMS_SENDING_BLOCK,
    SMS_SENDING_ERROR
}
