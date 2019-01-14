package ru.surfstudio.android.core.mvp.rx.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Rx-обертка над действиями пользователя
 * За отправку событий отвечает View
 * Подписка на события происходит только внутри Presenter
 */
class Action<T> : Relation<T, VIEW, PRESENTER> {

    private val relay = BehaviorRelay.create<T>()

    override var hasValued: Boolean = relay.hasValue()

    override val value: T get() = relay.value ?: throw NoSuchElementException()

    override fun getConsumer(source: VIEW): Consumer<T> = relay

    override fun getObservable(target: PRESENTER): Observable<T> = relay
}