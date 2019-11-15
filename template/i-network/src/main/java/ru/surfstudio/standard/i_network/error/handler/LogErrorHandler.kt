package ru.surfstudio.standard.i_network.error.handler

import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.i_network.error.exception.BaseWrappedHttpException

/**
 * Пример реализации [BaseErrorHandler]
 * с выводом обёрнутого сообщения в лог
 */
class LogErrorHandler : BaseErrorHandler {

    override fun handle(e: BaseWrappedHttpException) {
        Logger.e("Network error occurred: $e")
    }
}