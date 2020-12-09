package ru.surfstudio.standard.f_splash

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal class SplashState()

/**
 * State Holder [SplashActivityView]
 */
@PerScreen
internal class SplashScreenStateHolder @Inject constructor(
) : State<SplashState>(SplashState())

/**
 * Reducer [SplashActivityView]
 */
@PerScreen
internal class SplashReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<SplashEvent, SplashState>(dependency) {

    override fun reduce(state: SplashState, event: SplashEvent): SplashState {
        return state
    }
}