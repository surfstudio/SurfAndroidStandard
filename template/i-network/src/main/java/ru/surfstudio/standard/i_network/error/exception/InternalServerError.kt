package ru.zenit.android.interactor.common.network.error.http

import ru.surfstudio.standard.i_network.error.ApiErrorCode
import ru.surfstudio.standard.i_network.error.exception.BaseWrappedHttpException
import ru.surfstudio.standard.i_network.error.exception.HttpProtocolException

/**
 * Внутренняя ошибка сервера: [ApiErrorCode.INTERNAL_SERVER_ERROR]
 */
class InternalServerError(
        message: String,
        source: HttpProtocolException
) : BaseWrappedHttpException(message, source)