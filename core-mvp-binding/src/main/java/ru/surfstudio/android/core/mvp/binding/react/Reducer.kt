package ru.surfstudio.android.core.mvp.binding.react

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER

interface Reducer : Related<PRESENTER> {
    fun <T : Event> react(event: T)

    override fun relationEntity() = PRESENTER

    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable {
        throw NotImplementedError("Reducer cant manage subscription lifecycle")
    }
}