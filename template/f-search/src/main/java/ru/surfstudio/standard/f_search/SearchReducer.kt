package ru.surfstudio.standard.f_search

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal class SearchState()

/**
 * State Holder [SearchFragmentView]
 */
@PerScreen
internal class SearchScreenStateHolder @Inject constructor(
) : State<SearchState>(SearchState())

/**
 * Reducer [SearchFragmentView]
 */
@PerScreen
internal class SearchReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<SearchEvent, SearchState>(dependency) {

    override fun reduce(state: SearchState, event: SearchEvent): SearchState {
        return state
    }
}