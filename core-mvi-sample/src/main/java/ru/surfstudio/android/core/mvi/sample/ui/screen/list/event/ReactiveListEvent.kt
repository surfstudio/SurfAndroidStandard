package ru.surfstudio.android.core.mvi.sample.ui.screen.list.event

import ru.surfstudio.android.core.mvi.event.*
import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage

sealed class ReactiveListEvent : Event {
    class Reload : ReactiveListEvent()
    class LoadNextPage : ReactiveListEvent(), BaseLoadNextPageEvent
    class SwipeRefresh : ReactiveListEvent(), BaseSwipeRefreshEvent

    data class LoadReactiveList(
            var isSwr: Boolean
    ) : LoadableEvent<DataList<String>> by LoadableEventImpl(), ReactiveListEvent()

    data class QueryChanged(val query: String) : ReactiveListEvent()
    data class QueryChangedDebounced(val query: String) : ReactiveListEvent()

    class FilterNumbers : ReactiveListEvent()

    data class LifecycleChanged(override var stage: LifecycleStage) : ReactiveListEvent(), LifecycleEvent
}