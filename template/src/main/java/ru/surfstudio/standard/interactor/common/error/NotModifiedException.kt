package ru.surfstudio.standard.interactor.common.error

import retrofit2.adapter.rxjava.HttpException

/**
 * ошибка 304, см механизм Etag
 */
class NotModifiedException(cause: HttpException, message: String, code: Int, url: String) : HttpProtocolException(cause, message, code, url)
