package ru.surfstudio.standard.ui.base.error


import android.text.TextUtils

import javax.inject.Inject

import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.app.log.Logger
import ru.surfstudio.android.core.ui.base.message.MessageController
import ru.surfstudio.standard.R
import ru.surfstudio.standard.interactor.common.error.ConversionException
import ru.surfstudio.standard.interactor.common.error.HttpCodes
import ru.surfstudio.standard.interactor.common.error.HttpProtocolException
import ru.surfstudio.standard.interactor.common.error.NoInternetException

/**
 * Стандартный обработчик ошибок, возникающих при работе с сервером
 */
@PerScreen
open class StandardErrorHandler @Inject
constructor(private val messageController: MessageController) : NetworkErrorHandler() {

    override fun handleHttpProtocolException(e: HttpProtocolException) {
        if (e.code >= HttpCodes.CODE_500) {
            messageController.show(R.string.server_error_message)
        } else if (e.code == HttpCodes.CODE_403) {
            messageController.show(R.string.forbidden_error_error_message)
        } else if (!TextUtils.isEmpty(e.httpMessage)) {
            messageController.show(e.httpMessage)
        } else if (e.code == HttpCodes.CODE_404) {
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
