package ru.surfstudio.standard.interactor.auth.network.service;


import io.reactivex.Observable;
import ru.surfstudio.android.network.error.HttpCodes;
import ru.surfstudio.standard.interactor.common.network.error.HttpProtocolException;
import ru.surfstudio.standard.interactor.common.network.error.handler.BaseErrorHandler;

/**
 * Бзовый класс для работы с api
 */

public class BaseNetworkService {

    protected <T> Observable<T> call(Observable<T> observable, BaseErrorHandler errorHandler) {
        return observable.onErrorResumeNext((Throwable throwable) -> handleError(throwable, errorHandler));
    }

    private <T> Observable<T> handleError(Throwable throwable, BaseErrorHandler errorHandler) {
        if (throwable instanceof HttpProtocolException) {
            HttpProtocolException httpException = (HttpProtocolException) throwable;
            int httpCode = httpException.getHttpCode();

            if (httpCode == HttpCodes.CODE_400) {
                errorHandler.handle(httpException);
            }
        }

        return Observable.error(throwable);
    }
}
