package ru.surfstudio.android.mvp.binding.rx.sample.react.reducer

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableState
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableType
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.*
import ru.surfstudio.android.core.mvp.binding.react.optional.asOptional
import ru.surfstudio.android.core.mvp.binding.react.reactor.StatefulReactor
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.LoadListEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.QueryChangedEvent
import javax.inject.Inject

@PerScreen
class ListFeature @Inject constructor() : StatefulReactor {

    val state = LoadableState<DataList<String>>()

    override fun <T : Event> react(event: T) {
        when (event) {
            is LoadListEvent -> reactOnListLoadEvent(event)
            is QueryChangedEvent -> reactOnQueryChangedEvent(event)
        }
    }

    private fun reactOnListLoadEvent(event: LoadListEvent) {
        state.modify {
            when (event.type) {
                LoadableType.Data -> {
                    copy(
                            data = if (data.hasValue) {
                                data.get().merge(event.data.get()).asOptional()
                            } else {
                                event.data
                            },
                            load = event.mapLoading(data.hasValue)
                    )
                }

                LoadableType.Error -> {
                    copy(
                            error = if (!data.hasValue) CommonError(event.error) else EmptyError(),
                            load = event.mapLoading(data.hasValue)
                    )
                }

                LoadableType.Loading -> {
                    copy(load = event.mapLoading(data.hasValue))
                }
            }
        }
    }

    private fun <T> LoadableEvent<T>.mapLoading(hasData: Boolean, isSwr: Boolean = false) =
            if (hasData) {
                if (isSwr) {
                    SwipeRefreshLoading(isLoading)
                } else {
                    TransparentLoading(isLoading)
                }
            } else {
                MainLoading(isLoading)
            }

    private fun reactOnQueryChangedEvent(event: QueryChangedEvent) {
        state.modify {
            copy(data = DataList(
                    data.get().filter { elem -> elem.contains(event.query, true) },
                    1,
                    data.get().pageSize
            ).asOptional())
        }
    }
}
