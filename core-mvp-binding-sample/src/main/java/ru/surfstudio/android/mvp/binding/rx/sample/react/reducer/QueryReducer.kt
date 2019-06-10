package ru.surfstudio.android.mvp.binding.rx.sample.react.reducer

import ru.surfstudio.android.core.mvp.binding.react.Event
import ru.surfstudio.android.core.mvp.binding.react.Reducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.QueryChangedEvent

class QueryReducer : Reducer {

    val queryState = State<String>()

    override fun <T : Event> react(event: T) {
        when (event) {
            is QueryChangedEvent -> {
                queryState.accept(event.query)
            }
        }
    }
}