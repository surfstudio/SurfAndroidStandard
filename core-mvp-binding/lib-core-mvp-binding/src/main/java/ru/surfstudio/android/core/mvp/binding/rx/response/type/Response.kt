package ru.surfstudio.android.core.mvp.binding.rx.response.type

import io.reactivex.Observable

/**
 * Тип загрузки данных уровня Interactor.
 *
 * Отражает цикл асинхронной загрузки данных:
 *
 * 1. Получение данных начинается с состояния загрузки [Response.Loading],
 * 2. После этого либо приходят данные [Response.Data],
 * 3. Либо приходит ошибка загрузки данных [Response.Error]
 */
sealed class Response<T> {
    class Loading<T> : Response<T>()
    data class Data<T>(val data: T) : Response<T>()
    data class Error<T>(val error: Throwable) : Response<T>()
}

/**
 * Расширение для Observable, переводящее асинхронный запрос загрузки данных к Observable<[Response]>.
 */
fun <T> Observable<T>.mapResponse(): Observable<Response<T>> = this
        .map { Response.Data(it) as Response<T> }
        .startWith(Response.Loading())
        .onErrorReturn { t: Throwable -> Response.Error(t) }