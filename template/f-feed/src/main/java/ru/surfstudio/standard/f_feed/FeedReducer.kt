package ru.surfstudio.standard.f_feed

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal class FeedState()

/**
 * State Holder [FeedFragmentView]
 */
@PerScreen
internal class FeedScreenStateHolder @Inject constructor(
) : State<FeedState>(FeedState())

/**
 * Reducer [FeedFragmentView]
 */
@PerScreen
internal class FeedReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<FeedEvent, FeedState>(dependency) {

    override fun reduce(state: FeedState, event: FeedEvent): FeedState {
        return state
    }
}