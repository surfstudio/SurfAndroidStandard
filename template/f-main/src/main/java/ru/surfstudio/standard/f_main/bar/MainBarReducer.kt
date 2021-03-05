package ru.surfstudio.standard.f_main.bar

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_main.bar.MainBarEvent.TabSelected
import ru.surfstudio.standard.ui.navigation.routes.MainTabType
import javax.inject.Inject

internal data class MainBarState(
        val selectedTab: MainTabType = MainTabType.FEED
)

@PerScreen
internal class MainBarStateHolder @Inject constructor() : State<MainBarState>(MainBarState())

@PerScreen
internal class MainBarReducer @Inject constructor(
        dependency: BaseReactorDependency
) : BaseReducer<MainBarEvent, MainBarState>(dependency) {

    override fun reduce(state: MainBarState, event: MainBarEvent): MainBarState {
        return when (event) {
            is TabSelected -> state.copy(selectedTab = event.tabType)
            else -> state
        }
    }
}
