package ru.surfstudio.android.mvp.binding.rx.sample.react.reducer

import ru.surfstudio.android.core.mvp.binding.react.Event
import ru.surfstudio.android.core.mvp.binding.react.Reducer
import ru.surfstudio.android.core.mvp.binding.react.optional.Optional
import ru.surfstudio.android.core.mvp.binding.react.optional.valueOrNull
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.LoadListNetworkRequest

class ListReducer : Reducer {

    val data = State<List<String>>()
    val isLoading = State<Boolean>()
    val error = State<Throwable>()

    override fun <T : Event> react(event: T) {
        when (event) {
            is LoadListNetworkRequest -> {
                event.data.valueOrNull?.let { data.accept(it) }
                isLoading.accept(event.isLoading)

            }
        }
    }
}