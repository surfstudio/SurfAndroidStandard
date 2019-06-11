package ru.surfstudio.android.mvp.binding.rx.sample.react.reducer

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.RequestType
import ru.surfstudio.android.core.mvp.binding.react.reactor.Feature
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ListRxEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.QueryChangedEvent
import javax.inject.Inject

class ListFeature @Inject constructor() : Feature {

    val list = State<DataList<String>>()
    val isLoading = State<Boolean>()
    val error = State<Throwable>()

    override fun <T : Event> react(event: T) {
        when (event) {
            is ListRxEvent -> reactOnListLoadEvent(event)
            is QueryChangedEvent -> reactOnQueryChangedEvent(event)
        }
    }

    private fun reactOnListLoadEvent(event: ListRxEvent) {
        when (event.type) {
            RequestType.Data -> {
                val data = event.data.forcedValue
                if (list.hasValue) list.change { it.merge(data) } else list.accept(data)
//                error.accept(Empty)
            }

            RequestType.Loading -> {

            }

            RequestType.Error -> {
                if (!list.hasValue) error.accept(event.error.forcedValue)
            }
        }
        isLoading.accept(event.isLoading && !list.hasValue)
    }


    private fun reactOnQueryChangedEvent(event: QueryChangedEvent) {
        list.change {
            DataList(it.filter { elem -> elem.contains(event.query, true) }, 1, it.pageSize)
        }
    }
}
