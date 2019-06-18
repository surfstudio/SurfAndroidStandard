package ru.surfstudio.android.core.mvp.binding.react.reactor

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.binding.react.event.Event

interface Reactor<T : Event, H : StateHolder> : Reducer<T, H> {

    @CallSuper
    override fun reduce(state: H, event: T): H {
        react(state, event)
        return state
    }

    fun react(holder: H, event: T)
}