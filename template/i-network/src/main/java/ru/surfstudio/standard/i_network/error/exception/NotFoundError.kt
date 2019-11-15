package ru.surfstudio.standard.i_network.error.exception

import ru.surfstudio.standard.i_network.error.ApiErrorCode

/**
 * Ошибка возникающая при отсутствии запрашиваемых данных: [ApiErrorCode.NOT_FOUND]
 */
class NotFoundError(
        message: String,
        source: HttpProtocolException
) : BaseWrappedHttpException(message, source)