package ru.surfstudio.android.mvp.binding.rx.sample.react.event

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleStage
import ru.surfstudio.android.core.mvp.binding.react.loadable.event.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.react.loadable.event.LoadType
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList

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

    data class LifecycleChanged(override var stage: LifecycleStage) : ReactiveListEvent(), LifecycleEvent
}