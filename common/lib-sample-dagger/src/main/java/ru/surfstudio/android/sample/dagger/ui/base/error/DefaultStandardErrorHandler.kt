package ru.surfstudio.android.sample.dagger.ui.base.error

import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.sample.common.R
import ru.surfstudio.android.sample.dagger.app.exceptions.ConversionException
import ru.surfstudio.android.sample.dagger.app.exceptions.NoInternetException
import javax.inject.Inject

/**
 * Стандартный обработчик ошибок, возникающих при работе с сервером
 */
@PerScreen
open class DefaultStandardErrorHandler @Inject
constructor(private val messageController: MessageController) : DefaultNetworkErrorHandler() {

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
