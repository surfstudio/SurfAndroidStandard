package ru.surfstudio.standard.interactor.auth.network.error.parameter

import ru.surfstudio.standard.interactor.common.network.error.BaseMessagedException

/**
 * Ошибка сообщающая о неверном параметре
 */

data class IllegalParameterException(private val userMessage: String, private val error: IllegalParameterError)
    : BaseMessagedException(userMessage)
