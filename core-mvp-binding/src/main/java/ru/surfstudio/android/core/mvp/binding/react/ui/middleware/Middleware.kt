package ru.surfstudio.android.core.mvp.binding.react.ui.middleware

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.loadable.event.LoadType
import ru.surfstudio.android.core.mvp.binding.react.loadable.event.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import java.lang.IllegalStateException

interface Middleware<T : Event> : Related<PRESENTER> {

    fun transform(eventStream: Observable<T>): Observable<out T> =
            eventStream.flatMap(this::flatMap)

    fun flatMap(event: T): Observable<out T>

    override fun relationEntity(): PRESENTER = PRESENTER

    @CallSuper
    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable {
        throw IllegalStateException()
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

}