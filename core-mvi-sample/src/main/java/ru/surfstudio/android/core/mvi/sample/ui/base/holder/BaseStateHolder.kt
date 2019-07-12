package ru.surfstudio.android.core.mvi.sample.ui.base.holder

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.reactor.RxStateHolder
import ru.surfstudio.android.core.mvi.ui.reactor.StateEventProvider

open class BaseStateHolder<E : Event> : RxStateHolder<E> {
    override val eventProviders: List<StateEventProvider<out E, *>> = listOf()
}