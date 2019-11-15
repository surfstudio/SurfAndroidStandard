package ru.surfstudio.standard.i_network.error.exception

import ru.surfstudio.standard.i_network.error.ApiErrorCode

/**
 * Ошибка сервера с кодом [ApiErrorCode.NOT_MODIFIED]
 */
class NotModifiedException(
        message: String,
        source: HttpProtocolException
) : BaseWrappedHttpException(message, source)