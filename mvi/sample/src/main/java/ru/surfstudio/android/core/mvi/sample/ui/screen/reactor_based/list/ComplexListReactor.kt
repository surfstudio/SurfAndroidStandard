package ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.list

import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapDataList
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapError
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.mapLoading
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.list.ComplexListEvent.*
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.request.state.RequestState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.pagination.PaginationState
import javax.inject.Inject

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

    override fun react(sh: ComplexListStateHolder, event: ComplexListEvent) {
        when (event) {
            is FilterNumbers -> onFilterNumbers(sh, event)
            is LoadList -> onLoadList(sh, event)
            is QueryChangedDebounced -> sh.query.accept(event.query)
        }
    }

    private fun onLoadList(sh: ComplexListStateHolder, event: LoadList) {
        sh.list.modify {
            val hasData = data?.isNotEmpty() ?: false
            copy(
                    data = mapDataList(event.request, data, hasData),
                    load = mapLoading(event.request, hasData, event.isSwr),
                    error = mapError(event.request, hasData)
            )
        }
    }

    private fun onFilterNumbers(sh: ComplexListStateHolder, event: FilterNumbers) {
        val data = sh.list.data ?: return
        val query = if (sh.query.hasValue) sh.query.value else null
        val list = if (data.isNotEmpty() && !query.isNullOrEmpty()) {
            data.filter { it.contains(query, true) }
        } else {
            data
        }
        sh.filteredList.accept(list to event.state)
    }
}