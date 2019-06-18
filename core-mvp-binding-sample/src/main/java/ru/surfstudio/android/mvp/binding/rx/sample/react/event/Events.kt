package ru.surfstudio.android.mvp.binding.rx.sample.react.event

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.OpenScreen
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleStage
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadType
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList

sealed class ListEvent : Event {
    class Reload : ListEvent()
    class LoadNextPage : ListEvent()
    class SwipeRefresh : ListEvent()

    data class LoadList(
            override var type: LoadType<DataList<String>> = LoadType.Loading()
    ) : LoadableEvent<DataList<String>>, ListEvent()

    class QueryChanged(val query: String) : ListEvent()

    data class Lifecycle(override var stage: LifecycleStage) : ListEvent(), LifecycleEvent
}