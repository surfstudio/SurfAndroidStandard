package ru.surfstudio.android.core.mvi.ui.reactor

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER

/**
 * Класс, реагирующий на событие изменением текущего состояния View.
 *
 * Чтобы не возвращаться к хранению всего состояния экрана в одном классе, мы не создаем каждый раз новое состояние,
 * а изменяем его, и даем View отреагировать на это с помощью Rx.
 */
interface Reactor<E : Event, H : StateHolder> : Related<PRESENTER> {

    fun react(holder: H, event: E)

    override fun relationEntity() = PRESENTER

    @CallSuper
    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable {
        throw NotImplementedError("Reactor cant manage subscription lifecycle")
    }

}