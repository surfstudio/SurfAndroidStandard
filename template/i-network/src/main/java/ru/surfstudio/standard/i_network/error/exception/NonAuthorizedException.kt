package ru.surfstudio.standard.i_network.error.exception

import ru.surfstudio.standard.i_network.error.ApiErrorCode

/**
 * Ошибка при отсутствии авторизации: [ApiErrorCode.NOT_AUTHORIZED]
 */
class NonAuthorizedException(
        message: String,
        cause: HttpProtocolException
): BaseWrappedHttpException(message, cause)