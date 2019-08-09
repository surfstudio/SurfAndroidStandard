package ru.surfstudio.android.core.mvi.ui.relation

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER

/**
 * Класс, который может изменять значение State, но не может подписываться на изменения.
 */
interface StateEmitter : Related<PRESENTER> {

    override fun relationEntity() = PRESENTER

    @CallSuper
    override fun <T> subscribe(observable: Observable<out T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable {
        throw NotImplementedError("StateEmitter cant manage subscription lifecycle")
    }
}