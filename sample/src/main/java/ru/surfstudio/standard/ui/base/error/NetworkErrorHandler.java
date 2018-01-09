package ru.surfstudio.standard.ui.base.error;

import java.util.List;

import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.core.ui.base.error.ErrorHandler;
import ru.surfstudio.standard.interactor.common.network.error.ConversionException;
import ru.surfstudio.standard.interactor.common.network.error.HttpProtocolException;
import ru.surfstudio.standard.interactor.common.network.error.NetworkException;
import ru.surfstudio.standard.interactor.common.network.error.NoContentException;
import ru.surfstudio.standard.interactor.common.network.error.NoInternetException;
import rx.exceptions.CompositeException;

/**
 * Базовый класс для обработки ошибок, возникающий при работе с Observable из слоя Interactor
 */
public abstract class NetworkErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable err) {
        Logger.i(err, "NetworkErrorHandler handle error");
        if (err instanceof CompositeException) {
            handleCompositeException((CompositeException) err);
        } else if (err instanceof ConversionException) {
            handleConversionError((ConversionException) err);
        } else if (err instanceof HttpProtocolException) {
            handleHttpProtocolException((HttpProtocolException) err);
        } else if (err instanceof NoInternetException) {
            handleNoInternetError((NoInternetException) err);
        } else if (err instanceof NoContentException) {
            handleNoContentError((NoContentException) err);
        } else {
            handleOtherError(err);
        }
    }

    /**
     * @param err - CompositeException может возникать при комбинировании Observable
     */
    private void handleCompositeException(CompositeException err) {
        List<Throwable> exceptions = err.getExceptions();
        NetworkException networkException = null;
        Throwable otherException = null;
        for (Throwable e : exceptions) {
            if (e instanceof NetworkException) {
                if (networkException == null) {
                    networkException = (NetworkException) e;
                }
            } else if (otherException == null) {
                otherException = e;
            }
        }
        if (networkException != null) {
            handleError(networkException);
        }
        if (otherException != null) {
            handleOtherError(otherException);
        }
    }

    protected abstract void handleHttpProtocolException(HttpProtocolException e);

    protected abstract void handleNoInternetError(NoInternetException e);

    protected abstract void handleNoContentError(NoContentException e);

    protected abstract void handleConversionError(ConversionException e);

    protected abstract void handleOtherError(Throwable e);
}
