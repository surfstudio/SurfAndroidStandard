package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.KittiesEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.Kitty
import ru.surfstudio.android.core.mvi.ui.reducer.Reducer
import ru.surfstudio.android.core.mvi.ui.reducer.RequestMapper
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal data class KittiesState(
        val loadTopKittyNameRequestUi: RequestUi<String> = RequestUi(),
        val loadKittiesCountRequestUi: RequestUi<Int> = RequestUi(),
        val loadKittiesListRequestUi: RequestUi<List<Kitty>> = RequestUi(),
        val sendMeowRequestUi: RequestUi<Unit> = RequestUi()
)

@PerScreen // TODO Почему в State<требуется_указать_класс?>
internal class KittiesStateHolder @Inject constructor() : State<KittiesState>(KittiesState())

@PerScreen
internal class KittiesReducer @Inject constructor() : Reducer<KittiesEvent, KittiesState> {
    override fun reduce(state: KittiesState, event: KittiesEvent): KittiesState {
        return when (event) {
            is LoadTopKittyNameRequestEvent -> onLoadTopKittyName(state, event)
            is LoadKittiesCountRequestEvent -> onLoadKittiesCount(state, event)
            is LoadKittiesListRequestEvent -> onLoadKittiesList(state, event)
            is SendMeowRequestEvent -> onSendMeow(state, event)
            else -> state
        }
    }

    // TODO RequestMappers
    private fun onLoadTopKittyName(state: KittiesState, event: LoadTopKittyNameRequestEvent) : KittiesState {
        val newRequestUi = RequestMapper.builder(event.type, state.loadTopKittyNameRequestUi)
                .mapData { request, data -> request.dataOrNull ?: data }
                .build()
        return state.copy(loadTopKittyNameRequestUi = newRequestUi)
    }

    private fun onLoadKittiesCount(state: KittiesState, event: LoadKittiesCountRequestEvent): KittiesState {
        val newRequestUi = RequestMapper.builder(event.type, state.loadKittiesCountRequestUi)
                .mapData { request, data -> request.dataOrNull ?: data }
                .build()
        return state.copy(loadKittiesCountRequestUi = newRequestUi)
    }

    private fun onLoadKittiesList(state: KittiesState, event: LoadKittiesListRequestEvent): KittiesState {
        val newRequestUi = RequestMapper.builder(event.type, state.loadKittiesListRequestUi)
                // TODO pagination
                .mapData { request, data -> request.dataOrNull ?: data}
                .build()
        return state.copy(loadKittiesListRequestUi = newRequestUi)
    }

    private fun onSendMeow(state: KittiesState, event: SendMeowRequestEvent): KittiesState {
        val newRequestUi = RequestMapper.builder(event.type, state.sendMeowRequestUi)
                // TODO react
                .build()
        return state.copy(sendMeowRequestUi = newRequestUi)
    }
}