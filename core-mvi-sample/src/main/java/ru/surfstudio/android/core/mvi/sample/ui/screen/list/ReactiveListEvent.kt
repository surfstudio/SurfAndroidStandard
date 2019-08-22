package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import ru.surfstudio.android.core.mvi.event.*
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvp.binding.rx.loadable.type.Response
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.easyadapter.pagination.PaginationState

sealed class ReactiveListEvent : Event {
    class Reload : ReactiveListEvent()
    class LoadNextPage : ReactiveListEvent()
    class SwipeRefresh : ReactiveListEvent()

    data class LoadList(
            override var type: Response<DataList<String>> = Response.Loading(),
            var isSwr: Boolean
    ) : ResponseEvent<DataList<String>>, ReactiveListEvent()

    data class QueryChanged(val query: String) : ReactiveListEvent()
    data class QueryChangedDebounced(val query: String) : ReactiveListEvent()

    data class FilterNumbers(override var state: PaginationState = PaginationState.READY) : ReactiveListEvent(), PaginationEvent
    data class ReactiveListLifecycle(override var stage: LifecycleStage) : ReactiveListEvent(), LifecycleEvent
}