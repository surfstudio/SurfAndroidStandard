package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.all

import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.all.KittiesAllEvent.LoadKittiesRequestEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.KittenUi
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.util.PaginationBundle
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.util.RequestMappers
import ru.surfstudio.android.core.mvi.ui.mapper.RequestMapper
import ru.surfstudio.android.core.mvi.ui.reducer.Reducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.request.data.MainLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SwipeRefreshLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.TransparentLoading
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal data class KittiesAllState(
        val kittiesRequestUi: RequestUi<PaginationBundle<KittenUi>> = RequestUi()
) {
    val isSwr = (kittiesRequestUi.load as? SwipeRefreshLoading)?.isLoading ?: false
    val isMainLoading = (kittiesRequestUi.load as? MainLoading)?.isLoading ?: false
    val isTransparentLoading = (kittiesRequestUi.load as? TransparentLoading)?.isLoading ?: false
}

@PerScreen
internal class KittiesAllStateHolder @Inject constructor() :
        State<KittiesAllState>(KittiesAllState())

@PerScreen
internal class KittiesAllReducer @Inject constructor() :
        Reducer<KittiesAllEvent, KittiesAllState> {

    override fun reduce(state: KittiesAllState, event: KittiesAllEvent): KittiesAllState {
        return when (event) {
            is LoadKittiesRequestEvent -> onLoadKittiesRequest(state, event)
            else -> state
        }
    }

    private fun onLoadKittiesRequest(state: KittiesAllState, event: LoadKittiesRequestEvent): KittiesAllState {
        val newRequestUi = RequestMapper.builder(event.request, state.kittiesRequestUi)
                // Преобразуем Request<DataList<Kitten>> в Request<DataList<KittenUi>>
                .mapRequest { requestData -> requestData.transform { KittenUi(it) } }
                // Мапим данные пагинации специализированным маппером
                .mapData(RequestMappers.data.pagination())
                // Мапим состояние загрузки
                .mapLoading(RequestMappers.loading.pagination(event.isSwr))
                // Не обрабатываем ошибки намеренно, т.к это тест
                .build()
        return state.copy(kittiesRequestUi = newRequestUi)
    }
}