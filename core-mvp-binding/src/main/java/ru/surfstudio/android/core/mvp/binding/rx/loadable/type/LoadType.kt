package ru.surfstudio.android.core.mvp.binding.rx.loadable.type

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.rx.loadable.data.EmptyErrorException

/**
 * Тип загрузки данных уровня Interactor.
 *
 * Отражает цикл асинхронной загрузки данных:
 * 1. Получение данных начинается с состояния загрузки [LoadType.Loading],
 * 2. После этого либо приходят данные [LoadType.Data],
 * 3. Либо приходит ошибка загрузки данных [LoadType.Error]
 */
sealed class LoadType<T> {
    class Loading<T> : LoadType<T>()
    data class Data<T>(val data: T) : LoadType<T>()
    data class Error<T>(val error: Throwable = EmptyErrorException()) : LoadType<T>()
}

/**
 * Расширение для Observable, переводящее асинхронный запрос загрузки данных к Observable<[LoadType]>.
 */
fun <T> Observable<T>.mapToLoadType(): Observable<LoadType<T>> = this
        .map { LoadType.Data(it) as LoadType<T> }
        .startWith(LoadType.Loading())
        .onErrorReturn { t: Throwable -> LoadType.Error(t) }