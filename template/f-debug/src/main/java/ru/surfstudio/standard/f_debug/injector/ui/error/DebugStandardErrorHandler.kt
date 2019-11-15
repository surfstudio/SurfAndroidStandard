package ru.surfstudio.standard.f_debug.injector.ui.error

import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base.util.StringsProvider
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
open class DebugStandardErrorHandler @Inject constructor(
        private val messageController: MessageController,
        private val stringsProvider: StringsProvider
) : NetworkErrorHandler() {

    override fun handleHttpProtocolException(e: BaseWrappedHttpException) {
        if (e is NonAuthorizedException) {
            //TODO
            return
        }
        var message = e.displayMessage
        if (message.isEmpty()) {
            message = stringsProvider.getString(R.string.http_error_message_default)
        }
        messageController.show(message)
    }

    override fun handleNoInternetError(e: NoInternetException) {
        messageController.show(R.string.debug_no_internet_connection_error_message)
    }

    override fun handleConversionError(e: ConversionException) {
        messageController.show(R.string.debug_bad_response_error_message)
    }

    override fun handleOtherError(e: Throwable) {
        messageController.show(R.string.debug_unexpected_error_error_message)
        Logger.e(e, "Unexpected error")
    }
}