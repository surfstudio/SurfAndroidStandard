package ru.surfstudio.standard.interactor.common.network.error.handler

import ru.surfstudio.standard.interactor.common.network.error.HttpProtocolException

/**
 * Базовый класс обработки ошибок сервера
 */

interface BaseErrorHandler {

    fun handle(e: HttpProtocolException)
}
