package ru.surfstudio.android.core.mvi.sample.ui.screen.list.reactor

import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvp.binding.rx.loadable.state.LoadableState
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.event.ReactiveListEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.extension.mapDataList
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.extension.mapError
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.extension.mapLoading
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class ReactiveListStateHolder @Inject constructor() {
    val list = LoadableState<DataList<String>>()
    val query = State<String>()
    val filteredList = State<List<String>>()
}

@PerScreen
class ReactiveListReactor @Inject constructor() : Reactor<ReactiveListEvent, ReactiveListStateHolder> {

    override fun react(holder: ReactiveListStateHolder, event: ReactiveListEvent) {
        when (event) {
            is ReactiveListEvent.LoadNumbers -> reactOnListLoadEvent(holder, event)
            is ReactiveListEvent.FilterNumbers -> reactOnFilterEvent(holder)
            is ReactiveListEvent.QueryChangedDebounced -> holder.query.accept(event.query)
        }
    }

    private fun reactOnListLoadEvent(
            holder: ReactiveListStateHolder,
            event: ReactiveListEvent.LoadNumbers
    ) {
        holder.list.modify {
            val hasData = data.hasValue && !data.get().isEmpty()
            copy(
                    data = mapDataList(event.type, data, hasData),
                    load = mapLoading(event.type, hasData, event.isSwr),
                    error = mapError(event.type, hasData)
            )
        }
    }

    private fun reactOnFilterEvent(holder: ReactiveListStateHolder) {
        val data = holder.list.dataOrNull ?: return
        val query = if (holder.query.hasValue) holder.query.value else null
        val list = if (data.isNotEmpty() && !query.isNullOrEmpty()) {
            data.filter { it.contains(query, true) }
        } else {
            data
        }
        holder.filteredList.accept(list)
    }
}