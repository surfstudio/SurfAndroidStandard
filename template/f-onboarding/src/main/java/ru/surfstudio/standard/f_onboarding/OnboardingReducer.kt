package ru.surfstudio.standard.f_onboarding

import javax.inject.Inject
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen

class OnboardingState

@PerScreen
internal class OnboardingStateHolder @Inject constructor() : State<OnboardingState>(OnboardingState())

@PerScreen
internal class OnboardingReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<OnboardingEvent, OnboardingState>(dependency) {

    override fun reduce(state: OnboardingState, event: OnboardingEvent): OnboardingState {
        return state
    }

}
