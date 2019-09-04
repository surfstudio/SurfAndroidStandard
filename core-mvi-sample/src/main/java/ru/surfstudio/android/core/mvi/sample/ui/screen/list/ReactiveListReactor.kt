package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvp.binding.rx.response.state.ResponseState
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapDataList
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapError
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapLoading
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ReactiveListEvent.*
import ru.surfstudio.android.easyadapter.pagination.PaginationState

@PerScreen
class ReactiveListStateHolder @Inject constructor() {
    val list = ResponseState<DataList<String>>()
    val query = State<String>()
    val filteredList = State<Pair<List<String>, PaginationState>>()
}

@PerScreen
class ReactiveListReactor @Inject constructor() : Reactor<ReactiveListEvent, ReactiveListStateHolder> {

    override fun react(holder: ReactiveListStateHolder, event: ReactiveListEvent) {
        when (event) {
            is FilterNumbers -> onFilterNumbersEvent(holder, event)
            is LoadList -> onLoadListEvent(holder, event)
            is QueryChangedDebounced -> holder.query.accept(event.query)
        }
    }

    private fun onLoadListEvent(holder: ReactiveListStateHolder, event: LoadList) {
        holder.list.modify {
            val hasData = data.hasValue && !data.get().isEmpty()
            copy(
                    data = mapDataList(event.type, data, hasData),
                    load = mapLoading(event.type, hasData, event.isSwr),
                    error = mapError(event.type, hasData)
            )
        }
    }

    private fun onFilterNumbersEvent(holder: ReactiveListStateHolder, event: FilterNumbers) {
        val data = holder.list.dataOrNull ?: return
        val query = if (holder.query.hasValue) holder.query.value else null
        val list = if (data.isNotEmpty() && !query.isNullOrEmpty()) {
            data.filter { it.contains(query, true) }
        } else {
            data
        }
        holder.filteredList.accept(list to event.state)
    }
}