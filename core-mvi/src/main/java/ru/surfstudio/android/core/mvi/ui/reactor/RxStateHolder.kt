package ru.surfstudio.android.core.mvi.ui.reactor

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * Класс, содержащий состояние экрана
 */
interface RxStateHolder<E : Event> {
    val eventProviders: List<StateEventProvider<out E, *>>

    infix fun <T> State<T>.with(transformer: (T) -> E): StateEventProvider<E, T> =
            StateEventProvider(this, transformer)
}