package ru.surfstudio.android.core.mvi.ui.holder

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import java.lang.NullPointerException

/**
 * Наследник [ReducerStateHolder], работающий через Rx.
 */
open class RxStateHolder<S>(initialValue: S? = null) :
        ReducerStateHolder<S>() {

    private val relay = initialValue
            ?.let { BehaviorRelay.createDefault(it) }
            ?: BehaviorRelay.create<S>()

    override var state: S
        get() = relay.value ?: throw NullPointerException()
        set(value) {
            relay.accept(value)
        }

    /**
     * Подписка на изменение состояния экрана.
     * По-умолчанию работает фильтрация через distinctUntilChanged.
     */
    open fun observe(): Observable<S> = relay.share()
            .distinctUntilChanged { s1, s2 -> s1 == s2 }
}