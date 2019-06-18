package ru.surfstudio.android.core.mvp.binding.react.reactor

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER

interface Reducer<T : Event, H : StateHolder> : Related<PRESENTER> {

    fun reduce(state: H, event: T): H

    override fun relationEntity() = PRESENTER

    @CallSuper
    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable {
        throw NotImplementedError("Reactor cant manage subscription lifecycle")
    }

}