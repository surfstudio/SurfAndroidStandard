package ru.surfstudio.android.core.mvp.rx.domain

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import java.lang.IllegalArgumentException

/**
 * Rx-обертка над командами для View
 * Не является состоянием [State], так как команда должна быть показана только один раз
 *
 * За отправку событий отвечает Presenter
 * Подписывается на события View
 */
class Command<T>: Relation<T, PRESENTER, VIEW> {

    private val relay = PublishRelay.create<T>().toSerialized()

    override fun getSourceConsumer(source: PRESENTER): Consumer<T> = relay

    override fun getSourceObservable(source: PRESENTER): Observable<T> = relay

    override fun getTargetConsumer(target: VIEW): Consumer<T> = relay

    override fun getTargetObservable(target: VIEW): Observable<T> = relay
}