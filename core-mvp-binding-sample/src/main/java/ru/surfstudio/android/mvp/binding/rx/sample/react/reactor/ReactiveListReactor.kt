package ru.surfstudio.android.mvp.binding.rx.sample.react.reactor

import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableState
import ru.surfstudio.android.core.mvp.binding.react.optional.asOptional
import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.StateHolder
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ReactiveListEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.extension.mapDataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.extension.mapError
import ru.surfstudio.android.mvp.binding.rx.sample.react.extension.mapLoading
import javax.inject.Inject


typealias ReactiveListState = LoadableState<DataList<String>>

@PerScreen
class ReactiveListStateHolder @Inject constructor() : StateHolder {
    val state = LoadableState<DataList<String>>()
}

@PerScreen
class ReactiveListReactor @Inject constructor() : Reactor<ReactiveListEvent, ReactiveListStateHolder> {

    override fun react(holder: ReactiveListStateHolder, event: ReactiveListEvent) {
        when (event) {
            is ReactiveListEvent.LoadNumbers -> reactOnListLoadEvent(holder.state, event)
            is ReactiveListEvent.QueryChangedDebounced -> reactOnQueryChangedEvent(holder.state, event)
        }
    }

    private fun reactOnListLoadEvent(state: ReactiveListState, event: ReactiveListEvent.LoadNumbers) {
        state.modify {
            val hasData = data.hasValue && !data.get().isEmpty()
            copy(
                    data = mapDataList(event.type, data, hasData),
                    load = mapLoading(event.type, hasData, event.isSwr),
                    error = mapError(event.type, hasData)
            )
        }
    }

    private fun reactOnQueryChangedEvent(state: ReactiveListState, event: ReactiveListEvent.QueryChangedDebounced) {
        state.modify {
            copy(data = DataList(
                    data.get().filter { elem -> elem.contains(event.query, true) },
                    1,
                    data.get().pageSize
            ).asOptional())
        }
    }
}