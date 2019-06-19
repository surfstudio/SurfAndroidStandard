package ru.surfstudio.android.mvp.binding.rx.sample.react.event

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleStage
import ru.surfstudio.android.core.mvp.binding.react.loadable.event.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.react.loadable.event.LoadType
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList

sealed class ReactiveList : Event {
    class Reload : ReactiveList()
    class LoadNextPage : ReactiveList()
    class SwipeRefresh : ReactiveList()

    data class Numbers(
            override var type: LoadType<DataList<String>> = LoadType.Loading(),
            var isSwr: Boolean
    ) : LoadableEvent<DataList<String>>, ReactiveList()

    class QueryChanged(val query: String) : ReactiveList()

    data class Lifecycle(override var stage: LifecycleStage) : ReactiveList(), LifecycleEvent
}