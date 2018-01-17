package ru.surfstudio.standard.interactor.auth.network.error.sms


import ru.surfstudio.standard.interactor.common.network.error.BaseMessagedException

/**
 * Исключение описывающее проблемы при отсылке смс сервером для телефона
 */


data class SmsSendingException(private val userMessage: String, private val error: SmsSendingError)
    : BaseMessagedException(userMessage)
