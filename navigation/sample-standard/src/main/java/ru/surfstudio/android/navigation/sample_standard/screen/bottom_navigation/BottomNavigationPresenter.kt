package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.home.HomeFragmentRoute
import javax.inject.Inject

@PerScreen
class BottomNavigationPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val commandExecutor: NavigationCommandExecutor
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        openTab(BottomTabType.HOME)
    }

    private fun openTab(type: BottomTabType) = commandExecutor.execute(Replace(
            when (type) {
                BottomTabType.HOME -> HomeFragmentRoute()
                else -> TODO()
            }
    ))
}