package ru.surfstudio.standard.ui.error

import android.text.TextUtils
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.i_network.error.ApiErrorCode
import ru.surfstudio.standard.i_network.error.NetworkErrorHandler
import ru.surfstudio.standard.i_network.error.exception.BaseWrappedHttpException
import ru.surfstudio.standard.i_network.error.exception.NonAuthorizedException
import ru.surfstudio.standard.i_network.network.error.ConversionException
import ru.surfstudio.standard.i_network.network.error.NoInternetException
import javax.inject.Inject

/**
 * Стандартный обработчик ошибок, возникающих при работе с сервером
 */
@PerScreen
open class StandardErrorHandler @Inject constructor(
        private val messageController: MessageController
) : NetworkErrorHandler() {

    override fun handleHttpProtocolException(e: BaseWrappedHttpException) {
        if (e is NonAuthorizedException) {
            //TODO
            return
        }
        
        val httpException = e.httpCause
        
        if (httpException.httpCode >= ApiErrorCode.INTERNAL_SERVER_ERROR.code) {
            messageController.show(R.string.debug_server_error_message)
        } else if (httpException.httpCode == ApiErrorCode.FORBIDDEN.code) {
            messageController.show(R.string.debug_forbidden_error_error_message)
        } else if (!TextUtils.isEmpty(httpException.httpMessage)) {
            Logger.e(httpException.httpMessage)
        } else if (httpException.httpCode == ApiErrorCode.NOT_FOUND.code) {
            messageController.show(R.string.debug_server_error_not_found)
        } else {
            messageController.show(R.string.debug_default_http_error_message)
        }
    }

    override fun handleNoInternetError(e: NoInternetException) {
        messageController.show(R.string.no_internet_connection_error_message)
    }

    override fun handleConversionError(e: ConversionException) {
        messageController.show(R.string.bad_response_error_message)
    }

    override fun handleOtherError(e: Throwable) {
        messageController.show(R.string.unexpected_error_error_message)
        Logger.e(e, "Unexpected error")
    }
}