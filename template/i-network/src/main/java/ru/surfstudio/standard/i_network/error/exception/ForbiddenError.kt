package ru.surfstudio.standard.i_network.error.exception

import ru.surfstudio.standard.i_network.error.ApiErrorCode

/**
 * Ошибка запрета доступа: [ApiErrorCode.FORBIDDEN]
 */
class ForbiddenError(
        message: String,
        source: HttpProtocolException
) : BaseWrappedHttpException(message, source)