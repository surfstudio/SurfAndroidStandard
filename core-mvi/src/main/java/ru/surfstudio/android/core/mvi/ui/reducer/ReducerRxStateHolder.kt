package ru.surfstudio.android.core.mvi.ui.reducer

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.reactor.RxStateHolder
import ru.surfstudio.android.core.mvi.ui.reactor.StateEventProvider
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import java.lang.NullPointerException

/**
 * Наследник [ReducerStateHolder], работающий через Rx.
 */
open class ReducerRxStateHolder<S : Event>(initialValue: S) :
        ReducerStateHolder<S>,
        RxStateHolder<S>,
        State<S>(initialValue) {

    final override val eventProviders: List<StateEventProvider<out S, *>> by lazy {
        listOf(StateEventProvider(this) { it })
    }

    override var state: S
        get() = relay.value ?: throw NullPointerException()
        set(value) {
            relay.accept(value)
        }
}