package ru.surfstudio.android.core.mvp.rx.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import java.lang.IllegalArgumentException

/**
 * Rx-обертка над действиями пользователя
 * За отправку событий отвечает View
 * Подписка на события происходит только внутри Presenter
 */
class Action<T> : Relation<T, VIEW, PRESENTER> {

    private val relay = BehaviorRelay.create<T>()

    override val value: T get() = relay.value ?: throw NoSuchElementException()

    override fun getSourceConsumer(source: VIEW): Consumer<T> = relay

    override fun getSourceObservable(source: VIEW): Observable<T> = relay

    override fun getTargetConsumer(target: PRESENTER): Consumer<T> = relay

    override fun getTargetObservable(target: PRESENTER): Observable<T> = relay
}