package ru.surfstudio.android.mvp.binding.rx.sample.react.reducer

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableState
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableType
import ru.surfstudio.android.core.mvp.binding.react.optional.Optional
import ru.surfstudio.android.core.mvp.binding.react.optional.asOptional
import ru.surfstudio.android.core.mvp.binding.react.reactor.Feature
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.LoadListEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.OpenProfileScreen
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.QueryChangedEvent
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

@PerScreen
class ListFeature @Inject constructor() : Feature {

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
                            isLoading = false
                    )
                }

                LoadableType.Error -> {
                    copy(
                            error = if (!data.hasValue) event.error else Optional.Empty,
                            isLoading = false
                    )
                }

                LoadableType.Loading -> {
                    copy(isLoading = !data.hasValue)
                }

            }
        }
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
