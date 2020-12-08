package ru.surfstudio.standard.f_splash

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * todo Класс публичный для работы инструментальных тестов
 */
class SplashState()

/**
 * State Holder [SplashActivityView]
 * todo Класс публичный для работы инструментальных тестов
 */
@PerScreen
class SplashScreenStateHolder @Inject constructor(
) : State<SplashState>(SplashState())

/**
 * Reducer [SplashActivityView]
 * todo Класс публичный для работы инструментальных тестов
 */
@PerScreen
class SplashReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<SplashEvent, SplashState>(dependency) {

    override fun reduce(state: SplashState, event: SplashEvent): SplashState {
        return state
    }
}