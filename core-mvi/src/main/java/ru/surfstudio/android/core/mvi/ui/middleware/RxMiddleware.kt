package ru.surfstudio.android.core.mvi.ui.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.loadable.event.LoadType
import ru.surfstudio.android.core.mvi.loadable.event.LoadableEvent
import ru.surfstudio.android.core.mvi.ui.relation.StateObserver

/**
 * [Middleware] с реализацией в Rx.
 *
 * Кроме типизации по [Observable], содержит некоторые вспомогательные функции для работы с потоком.
 */
interface RxMiddleware<T : Event> : Middleware<T, Observable<T>, Observable<out T>>, StateObserver {

    fun <T, E : LoadableEvent<T>> Observable<T>.mapToLoadable(event: E): Observable<out E> =
            this
                    .map { event.apply { type = LoadType.Data(it) } }
                    .startWith(event.apply { type = LoadType.Loading() })
                    .onErrorReturn { event.apply { type = LoadType.Error(it) } }

    fun <T> skip() = Observable.empty<T>()

    fun <T> doAndSkip(action: () -> Unit): Observable<T> {
        action()
        return Observable.empty<T>()
    }

    fun Observable<T>.ignoreError() = onErrorResumeNext { _: Throwable -> Observable.empty() }

    fun merge(vararg observables: Observable<out T>): Observable<out T> = Observable.merge(observables.toList())
}