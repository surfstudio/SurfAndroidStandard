package ru.surfstudio.android.core.mvp.binding.react.reactor

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.binding.react.event.Event

/**
 * Доработанный [Reducer] для работы cо студийными биндингами:
 * Чтобы не возвращаться к хранению всего состояния экрана в одном классе, мы не изменяем это состояние,
 * а изменяем и даем View отреагировать на него с помощью Rx
 */
interface Reactor<T : Event, H : StateHolder> : Reducer<T, H> {

    @CallSuper
    override fun reduce(state: H, event: T): H {
        react(state, event)
        return state
    }

    fun react(holder: H, event: T)
}