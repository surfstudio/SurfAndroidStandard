package ru.surfstudio.android.core.mvp.binding.rx.request.type

import io.reactivex.Observable

/**
 * Тип загрузки данных уровня Interactor.
 *
 * Отражает цикл асинхронной загрузки данных:
 *
 * 1. Получение данных начинается с состояния загрузки [Request.Loading],
 * 2. После этого либо приходят данные [Request.Success],
 * 3. Либо приходит ошибка загрузки данных [Request.Error]
 */
sealed class Request<T> {
    class Loading<T> : Request<T>()
    data class Success<T>(val data: T) : Request<T>()
    data class Error<T>(val error: Throwable) : Request<T>()
}

/**
 * Расширение для Observable, переводящее асинхронный запрос загрузки данных к Observable<[Request]>.
 */
fun <T> Observable<T>.asRequest(): Observable<Request<T>> = this
        .map { Request.Success(it) as Request<T> }
        .startWith(Request.Loading())
        .onErrorReturn { t: Throwable -> Request.Error(t) }