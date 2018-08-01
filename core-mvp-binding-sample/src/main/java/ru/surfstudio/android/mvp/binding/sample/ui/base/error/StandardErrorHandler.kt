package ru.surfstudio.android.mvp.binding.sample.ui.base.error


import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.network.error.ConversionException
import ru.surfstudio.android.network.error.NoInternetException
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject

/**
 * Стандартный обработчик ошибок, возникающих при работе с сервером
 */
@PerScreen
open class StandardErrorHandler @Inject
constructor(private val messageController: MessageController) : NetworkErrorHandler() {

    override fun handleNoInternetError(e: NoInternetException) {
        messageController.show(EMPTY_STRING)
    }

    override fun handleConversionError(e: ConversionException) {
        messageController.show(EMPTY_STRING)
    }

    override fun handleOtherError(e: Throwable) {
        messageController.show(EMPTY_STRING)
        Logger.e(e, "Unexpected error")
    }
}
