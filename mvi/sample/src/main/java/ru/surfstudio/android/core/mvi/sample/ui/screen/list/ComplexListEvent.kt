package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import ru.surfstudio.android.core.mvi.event.*
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.pagination.PaginationEvent
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.pagination.PaginationState

/**
 * События экрана [ComplexListActivityView]
 */
sealed class ComplexListEvent : Event {
    class Reload : ComplexListEvent()
    class LoadNextPage : ComplexListEvent()
    class SwipeRefresh : ComplexListEvent()

    data class LoadList(
            override var type: Request<DataList<String>> = Request.Loading(),
            var isSwr: Boolean
    ) : RequestEvent<DataList<String>>, ComplexListEvent()

    data class QueryChanged(val query: String) : ComplexListEvent()
    data class QueryChangedDebounced(val query: String) : ComplexListEvent()

    data class FilterNumbers(override var state: PaginationState = PaginationState.READY) : ComplexListEvent(), PaginationEvent
    data class ComplexListLifecycle(override var stage: LifecycleStage) : ComplexListEvent(), LifecycleEvent
}