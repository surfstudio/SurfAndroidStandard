package ru.surfstudio.android.core.mvi.sample.ui.base.holder

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.holder.RxStateHolder
import ru.surfstudio.android.core.mvi.ui.effect.SideEffect

open class BaseStateHolder<E : Event> : RxStateHolder<E> {
    override val sideEffects: List<SideEffect<out E, *>> = listOf()
}