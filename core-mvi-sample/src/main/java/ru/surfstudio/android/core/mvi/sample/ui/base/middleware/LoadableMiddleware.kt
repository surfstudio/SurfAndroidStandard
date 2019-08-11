package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * Middleware с поддержкой загрузки данных.
 *
 * В проектах рекомендуется реализовать свой тип загрузки в зависимости от механизма пагинации
 */
interface LoadableMiddleware<T : Event> : RxMiddleware<T> {

    fun loadData(
            page: Int = 0,
            isSwr: Boolean = false
    ): Observable<out T>
}