package ru.surfstudio.standard.f_main

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

//todo Класс публичный для работы инструментальных тестов
class MainState()

/**
 * State Holder [MainActivityView]
 * todo Класс публичный для работы инструментальных тестов
 */
@PerScreen
class MainScreenStateHolder @Inject constructor(
) : State<MainState>(MainState())

/**
 * Reducer [MainActivityView]
 * todo Класс публичный для работы инструментальных тестов
 */
@PerScreen
class MainReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<MainEvent, MainState>(dependency) {

    override fun reduce(state: MainState, event: MainEvent): MainState {
        return state
    }
}