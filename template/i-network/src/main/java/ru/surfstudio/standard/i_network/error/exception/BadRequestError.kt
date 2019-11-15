package ru.surfstudio.standard.i_network.error.exception

import ru.surfstudio.standard.i_network.error.ApiErrorCode

/**
 * Ошибка при [ApiErrorCode.BAD_REQUEST]
 */
class BadRequestError(
        message: String,
        source: HttpProtocolException
): BaseWrappedHttpException(message, source)