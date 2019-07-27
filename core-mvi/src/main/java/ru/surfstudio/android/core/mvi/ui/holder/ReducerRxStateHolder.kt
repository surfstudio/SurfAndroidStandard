package ru.surfstudio.android.core.mvi.ui.holder

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import java.lang.NullPointerException

/**
 * Наследник [ReducerStateHolder], работающий через Rx.
 */
open class ReducerRxStateHolder<S : Event>(initialValue: S) :
        ReducerStateHolder<S>() {

    private val relay = BehaviorRelay.createDefault(initialValue)

    override var state: S
        get() = relay.value ?: throw NullPointerException()
        set(value) {
            relay.accept(value)
        }

    val observable: Observable<S> get() = relay.share()
}