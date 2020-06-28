package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.home.nested

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import javax.inject.Inject

@PerScreen
class HomeNestedPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: HomeNestedBindModel,
        private val commandExecutor: NavigationCommandExecutor,
        private val route: HomeNestedRoute
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        bm.currentOrder.accept(route.order)
        bm.openNextScreen.bindTo {
            commandExecutor.execute(
                    Replace(HomeNestedRoute(bm.currentOrder.value + 1)))
        }
    }
}