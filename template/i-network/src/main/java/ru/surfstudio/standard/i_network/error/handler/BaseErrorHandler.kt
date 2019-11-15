package ru.surfstudio.standard.i_network.error.handler

import ru.surfstudio.standard.i_network.error.exception.BaseWrappedHttpException

/**
 * Базовый класс обработки ошибок сервера
 */
interface BaseErrorHandler {

    fun handle(e: BaseWrappedHttpException)
}
