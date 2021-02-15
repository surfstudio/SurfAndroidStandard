package ru.surfstudio.standard.f_profile

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal class ProfileState()

/**
 * State Holder [ProfileFragmentView]
 */
@PerScreen
internal class ProfileScreenStateHolder @Inject constructor(
) : State<ProfileState>(ProfileState())

/**
 * Reducer [ProfileFragmentView]
 */
@PerScreen
internal class ProfileReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<ProfileEvent, ProfileState>(dependency) {

    override fun reduce(state: ProfileState, event: ProfileEvent): ProfileState {
        return state
    }
}