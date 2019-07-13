package ru.surfstudio.android.core.mvi.ui.middleware

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.loadable.event.LoadType
import ru.surfstudio.android.core.mvi.loadable.event.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import java.lang.IllegalStateException

/**
 * [Middleware] с реализацией в Rx.
 *
 * Кроме типизации по [Observable], содержит некоторые вспомогательные функции для работы с потоком.
 */
interface RxMiddleware<T : Event> : Middleware<T, Observable<T>, Observable<out T>>, Related<PRESENTER> {

    override fun relationEntity(): PRESENTER = PRESENTER

    @CallSuper
    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable {
        throw IllegalStateException("Middleware cant manage subscription lifecycle")
    }

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