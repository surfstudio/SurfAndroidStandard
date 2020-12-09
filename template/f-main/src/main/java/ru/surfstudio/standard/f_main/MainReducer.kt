package ru.surfstudio.standard.f_main

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal class MainState()

/**
 * State Holder [MainActivityView]
 */
@PerScreen
internal class MainScreenStateHolder @Inject constructor(
) : State<MainState>(MainState())

/**
 * Reducer [MainActivityView]
 */
@PerScreen
internal class MainReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<MainEvent, MainState>(dependency) {

    override fun reduce(state: MainState, event: MainEvent): MainState {
        return state
    }
}