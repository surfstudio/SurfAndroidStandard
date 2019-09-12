package ru.surfstudio.standard.ui.error

import android.text.TextUtils
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.standard.i_network.network.error.ConversionException
import ru.surfstudio.standard.i_network.network.error.HttpCodes
import ru.surfstudio.standard.i_network.network.error.NoInternetException
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.i_network.error.HttpProtocolException
import ru.surfstudio.standard.i_network.error.NetworkErrorHandler
import ru.surfstudio.standard.i_network.error.NonAuthorizedException
import javax.inject.Inject

/**
 * Стандартный обработчик ошибок, возникающих при работе с сервером
 */
@PerScreen
open class StandardErrorHandler @Inject constructor(
        private val messageController: MessageController,
        private val globalNavigator: GlobalNavigator
) : NetworkErrorHandler() {

    override fun handleHttpProtocolException(e: HttpProtocolException) {
        if (e is NonAuthorizedException) {
            //TODO
            return
        }

        if (e.httpCode >= HttpCodes.CODE_500) {
            messageController.show(R.string.server_error_message)
        } else if (e.httpCode == HttpCodes.CODE_403) {
            messageController.show(R.string.forbidden_error_error_message)
        } else if (!TextUtils.isEmpty(e.httpMessage)) {
            Logger.e(e.httpMessage)
        } else if (e.httpCode == HttpCodes.CODE_404) {
            messageController.show(R.string.server_error_not_found)
        } else {
            messageController.show(R.string.default_http_error_message)
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