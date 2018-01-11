package ru.surfstudio.standard.interactor.common.error.handler

import ru.surfstudio.standard.interactor.common.error.HttpProtocolException

/**
 * Базовый класс обработки ошибок сервера
 */

interface BaseErrorHandler {

    fun handle(e: HttpProtocolException)
}
