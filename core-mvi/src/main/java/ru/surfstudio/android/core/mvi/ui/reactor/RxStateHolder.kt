package ru.surfstudio.android.core.mvi.ui.reactor

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.provider.CommandEventProvider
import ru.surfstudio.android.core.mvi.ui.provider.EventProvider
import ru.surfstudio.android.core.mvi.ui.provider.ObservableEventProvider
import ru.surfstudio.android.core.mvi.ui.provider.StateEventProvider
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * Класс, содержащий состояние экрана
 *
 * @property eventProviders список пар <State, EventTransformer>, на которые необходимо подписаться
 */
interface RxStateHolder<E : Event> {
    val eventProviders: List<EventProvider<out E, *>>

    infix fun <T> Observable<T>.with(transformer: (T) -> E): EventProvider<E, T> =
            ObservableEventProvider(this, transformer)

    infix fun <T> State<T>.with(transformer: (T) -> E): EventProvider<E, T> =
            StateEventProvider(this, transformer)

    infix fun <T> Command<T>.with(transformer: (T) -> E): EventProvider<E, T> =
            CommandEventProvider(this, transformer)
}