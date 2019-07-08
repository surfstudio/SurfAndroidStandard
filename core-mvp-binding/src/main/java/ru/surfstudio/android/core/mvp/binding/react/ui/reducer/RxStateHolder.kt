package ru.surfstudio.android.core.mvp.binding.react.ui.reducer

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import java.lang.NullPointerException

/**
 * Наследник [ReducerStateHolder], работающий через Rx.
 */
open class RxStateHolder<S : ReducerState>(initialValue: S) :
        ReducerStateHolder<S>,
        State<S>(initialValue) {

    override var state: S
        get() = relay.value ?: throw NullPointerException()
        set(value) {
            relay.accept(value)
        }
}