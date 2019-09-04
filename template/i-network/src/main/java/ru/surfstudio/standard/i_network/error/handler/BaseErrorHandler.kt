package ru.surfstudio.standard.i_network.error.handler

import ru.surfstudio.standard.i_network.error.HttpProtocolException

/**
 * Базовый класс обработки ошибок сервера
 */
interface BaseErrorHandler {

    fun handle(e: HttpProtocolException)
}
