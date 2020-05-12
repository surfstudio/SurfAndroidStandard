package ru.surfstudio.android.sample.dagger.ui.base.error

import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.network.error.ConversionException
import ru.surfstudio.android.network.error.NetworkException
import ru.surfstudio.android.network.error.NoInternetException

/**
 * Base class to handle error which occur during Observable usage from Interactor
 */
abstract class DefaultNetworkErrorHandler : ErrorHandler {

    override fun handleError(err: Throwable) {
        Logger.i(err, "DefaultNetworkErrorHandler handle error")
        when (err) {
            is CompositeException -> handleCompositeException(err)
            is ConversionException -> handleConversionError(err)
            is NoInternetException -> handleNoInternetError(err)
            else -> handleOtherError(err)
        }
    }

    /**
     * @param err - CompositeException which could occur while combining Observable
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

    protected abstract fun handleNoInternetError(e: NoInternetException)

    protected abstract fun handleConversionError(e: ConversionException)

    protected abstract fun handleOtherError(e: Throwable)
}
