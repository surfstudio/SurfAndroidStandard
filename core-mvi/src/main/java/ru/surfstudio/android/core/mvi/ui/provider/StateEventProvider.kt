package ru.surfstudio.android.core.mvi.ui.provider

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.relation.StateObserver
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * [EventProvider] для [State]
 */
class StateEventProvider<E : Event, T>(
        private val state: State<T>,
        override val eventTransformer: (T) -> E
) : EventProvider<E, T>, StateObserver {
    override val observable: Observable<T>
        get() = state.observable
}