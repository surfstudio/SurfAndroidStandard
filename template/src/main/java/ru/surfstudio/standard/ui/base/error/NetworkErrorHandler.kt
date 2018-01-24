package ru.surfstudio.standard.ui.base.error

import ru.surfstudio.android.core.app.log.Logger
import ru.surfstudio.android.core.ui.base.error.ErrorHandler
import ru.surfstudio.android.network.error.ConversionException
import ru.surfstudio.android.network.error.NetworkException
import ru.surfstudio.android.network.error.NoInternetException
import ru.surfstudio.standard.interactor.common.network.error.HttpProtocolException
import rx.exceptions.CompositeException

/**
 * Базовый класс для обработки ошибок, возникающий при работе с Observable из слоя Interactor
 */
abstract class NetworkErrorHandler : ErrorHandler {

    override fun handleError(err: Throwable) {
        Logger.i(err, "NetworkErrorHandler handle error")
        if (err is CompositeException) {
            handleCompositeException(err)
        } else if (err is ConversionException) {
            handleConversionError(err)
        } else if (err is HttpProtocolException) {
            handleHttpProtocolException(err)
        } else if (err is NoInternetException) {
            handleNoInternetError(err)
        } else {
            handleOtherError(err)
        }
    }

    /**
     * @param err - CompositeException может возникать при комбинировании Observable
     */
    private fun handleCompositeException(err: CompositeException) {
        val exceptions = err.exceptions
        var networkException: NetworkException? = null
        var otherException: Throwable? = null
        for (e in exceptions) {
            if (e is NetworkException) {
                if (networkException == null) {
                    networkException = e
                }
            } else if (otherException == null) {
                otherException = e
            }
        }
        if (networkException != null) {
            handleError(networkException)
        }
        if (otherException != null) {
            handleOtherError(otherException)
        }
    }

    protected abstract fun handleHttpProtocolException(e: HttpProtocolException)

    protected abstract fun handleNoInternetError(e: NoInternetException)

    protected abstract fun handleConversionError(e: ConversionException)

    protected abstract fun handleOtherError(e: Throwable)
}
