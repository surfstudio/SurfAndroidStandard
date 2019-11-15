package ru.surfstudio.standard.i_network.error.exception

/**
 * Неизвестная ошибка, причина в [cause]
 */
class OtherHttpException(
        message: String,
        cause: HttpProtocolException
) : BaseWrappedHttpException(message, cause)