package ru.surfstudio.android.core.mvi.sample.ui.base.holder

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.reactor.RxStateHolder
import ru.surfstudio.android.core.mvi.ui.provider.EventProvider

open class BaseStateHolder<E : Event> : RxStateHolder<E> {
    override val eventProviders: List<EventProvider<out E, *>> = listOf()
}