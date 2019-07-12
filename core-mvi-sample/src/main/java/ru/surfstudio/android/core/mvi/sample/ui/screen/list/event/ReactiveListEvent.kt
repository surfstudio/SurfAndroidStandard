package ru.surfstudio.android.core.mvi.sample.ui.screen.list.event

import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleStage
import ru.surfstudio.android.core.mvi.loadable.event.LoadType
import ru.surfstudio.android.core.mvi.loadable.event.LoadableEvent
import ru.surfstudio.android.core.mvi.optional.Optional

sealed class ReactiveListEvent : Event {
    class Reload : ReactiveListEvent()
    class LoadNextPage : ReactiveListEvent()
    class SwipeRefresh : ReactiveListEvent()

    data class LoadNumbers(
            override var type: LoadType<DataList<String>> = LoadType.Loading(),
            var isSwr: Boolean
    ) : LoadableEvent<DataList<String>>, ReactiveListEvent()

    class QueryChanged(val query: String) : ReactiveListEvent()
    class QueryChangedDebounced(val query: String) : ReactiveListEvent()

    class FilterNumbers : ReactiveListEvent()

    sealed class Ui : ReactiveListEvent() {
        class ShowQuery(val query: String) : Ui()
        class ShowNumbers(val numbers: Optional<DataList<String>>) : Ui()
    }

    data class LifecycleChanged(override var stage: LifecycleStage) : ReactiveListEvent(), LifecycleEvent
}