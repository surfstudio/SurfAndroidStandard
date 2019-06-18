package ru.surfstudio.android.mvp.binding.rx.sample.react.reducer

import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadType
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableState
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.*
import ru.surfstudio.android.core.mvp.binding.react.optional.Optional
import ru.surfstudio.android.core.mvp.binding.react.optional.asOptional
import ru.surfstudio.android.core.mvp.binding.react.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.react.reactor.StateHolder
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ListEvent
import javax.inject.Inject


typealias ListState = LoadableState<DataList<String>>

@PerScreen
class ListStateHolder @Inject constructor() : StateHolder {
    val state = LoadableState<DataList<String>>()
}

@PerScreen
class ListReactor @Inject constructor() : Reactor<ListEvent, ListStateHolder> {

    override fun react(holder: ListStateHolder, event: ListEvent) {
        when (event) {
            is ListEvent.LoadList -> reactOnListLoadEvent(holder.state, event)
            is ListEvent.QueryChanged -> reactOnQueryChangedEvent(holder.state, event)
        }
    }

    private fun reactOnListLoadEvent(state: ListState, event: ListEvent.LoadList) {
        state.modify {
            copy(
                    data = mapDataList(event.type, data, data.hasValue),
                    load = mapLoading(event.type, data.hasValue),
                    error = mapError(event.type, data.hasValue)
            )
        }
    }


    private fun reactOnQueryChangedEvent(state: ListState, event: ListEvent.QueryChanged) {
        state.modify {
            copy(data = DataList(
                    data.get().filter { elem -> elem.contains(event.query, true) },
                    1,
                    data.get().pageSize
            ).asOptional())
        }
    }

    /**
     * Маппинг функции для работы с данными
     * TODO перенести их к Reducer, или куда-то в базовый класс/template
     */

    fun <T> mapLoading(type: LoadType<T>, hasData: Boolean, isSwr: Boolean = false): Loading {
        val isLoading = type is LoadType.Loading
        return if (hasData) {
            if (isSwr) {
                SwipeRefreshLoading(isLoading)
            } else {
                TransparentLoading(isLoading)
            }
        } else {
            MainLoading(isLoading)
        }
    }

    fun <T> mapError(type: LoadType<T>, hasData: Boolean): Throwable =
            if (!hasData && type is LoadType.Error) {
                type.error
            } else {
                EmptyErrorException()
            }

    fun <T> mapData(type: LoadType<T>, data: Optional<T>, hasData: Boolean): Optional<T> =
            if (type is LoadType.Data) {
                type.data.asOptional()
            } else {
                data
            }

    fun <T, L : DataList<T>> mapDataList(
            type: LoadType<L>,
            data: Optional<L>,
            hasData: Boolean
    ) = if (type is LoadType.Data) {
        if (hasData) {
            data.get().merge(type.data).asOptional()
        } else {
            type.data.asOptional()
        }
    } else {
        data
    }
}

