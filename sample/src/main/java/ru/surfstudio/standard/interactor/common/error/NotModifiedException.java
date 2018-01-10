package ru.surfstudio.standard.interactor.common.error;

import retrofit2.adapter.rxjava.HttpException;

/**
 * ошибка 304, см механизм Etag
 */
public class NotModifiedException extends HttpProtocolException {
    public NotModifiedException(HttpException cause, String message, int code, String url) {
        super(cause, message, code, url);
    }
}
