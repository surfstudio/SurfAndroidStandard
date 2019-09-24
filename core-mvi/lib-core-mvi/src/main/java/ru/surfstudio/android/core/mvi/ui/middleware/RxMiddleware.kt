package ru.surfstudio.android.core.mvi.ui.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter
import ru.surfstudio.android.core.mvp.binding.rx.response.type.asRequest

/**
 * [Middleware] с реализацией в Rx.
 *
 * Кроме типизации по [Observable], содержит некоторые вспомогательные функции для работы с потоком.
 */
interface RxMiddleware<T : Event> : Middleware<T, Observable<T>, Observable<out T>>, StateEmitter {

    fun <T, E : RequestEvent<T>> Observable<T>.asRequestEvent(
            event: E
    ): Observable<out E> = this
            .asRequest()
            .map { loadType ->
                event.type = loadType
                return@map event
            }

    fun <T> skip() = Observable.empty<T>()

    fun <T> doAndSkip(action: () -> Unit): Observable<T> {
        action()
        return Observable.empty<T>()
    }

    /**
     * Выполнение действия и пропуск дальнейшего проброса события.
     */
    fun <T> Any?.skip() = doAndSkip<T> { }

    fun merge(vararg observables: Observable<out T>): Observable<out T> = Observable.merge(observables.toList())
}