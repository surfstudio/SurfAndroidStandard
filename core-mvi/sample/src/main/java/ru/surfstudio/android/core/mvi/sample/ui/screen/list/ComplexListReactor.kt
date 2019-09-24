package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import ru.surfstudio.android.core.mvp.binding.rx.response.state.RequestState
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapDataList
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapError
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapLoading
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ComplexListEvent.*
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.pagination.PaginationState

/**
 * Хранитель состояния экрана [ComplexListActivityView]
 */
@PerScreen
class ComplexListStateHolder @Inject constructor() {
    val list = RequestState<DataList<String>>()
    val query = State<String>()
    val filteredList = State<Pair<List<String>, PaginationState>>()
}

/**
 * Реактор экрана [ComplexListActivityView]
 */
@PerScreen
class ComplexListReactor @Inject constructor() : Reactor<ComplexListEvent, ComplexListStateHolder> {

    override fun react(holder: ComplexListStateHolder, event: ComplexListEvent) {
        when (event) {
            is FilterNumbers -> onFilterNumbers(holder, event)
            is LoadList -> onLoadList(holder, event)
            is QueryChangedDebounced -> holder.query.accept(event.query)
        }
    }

    private fun onLoadList(holder: ComplexListStateHolder, event: LoadList) {
        holder.list.modify {
            val hasData = data.hasValue && !data.get().isEmpty()
            copy(
                    data = mapDataList(event.type, data, hasData),
                    load = mapLoading(event.type, hasData, event.isSwr),
                    error = mapError(event.type, hasData)
            )
        }
    }

    private fun onFilterNumbers(holder: ComplexListStateHolder, event: FilterNumbers) {
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