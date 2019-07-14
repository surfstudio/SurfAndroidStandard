package ru.surfstudio.android.core.mvi.ui.reducer

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.reactor.RxStateHolder
import ru.surfstudio.android.core.mvi.ui.effect.SideEffect
import ru.surfstudio.android.core.mvi.ui.effect.ObservableSideEffect
import java.lang.NullPointerException

/**
 * Наследник [ReducerStateHolder], работающий через Rx.
 */
open class ReducerRxStateHolder<S : Event>(initialValue: S) :
        ReducerStateHolder<S>(),
        RxStateHolder<S> {

    private val relay = BehaviorRelay.createDefault(initialValue)

    override var state: S
        get() = relay.value ?: throw NullPointerException()
        set(value) {
            relay.accept(value)
        }

    final override val sideEffects: List<SideEffect<out S, *>> =
            listOf(ObservableSideEffect(observable) { it })

    val observable: Observable<S> get() = relay.share()
}