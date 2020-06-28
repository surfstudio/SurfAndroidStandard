package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.activity.FinishAffinity
import ru.surfstudio.android.navigation.command.fragment.RemoveAll
import ru.surfstudio.android.navigation.command.fragment.RemoveLast
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.sample_standard.screen.base.command.CommandExecutionPresenter
import ru.surfstudio.android.navigation.scope.ScreenScopeNavigationProvider
import ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.home.HomeFragmentRoute
import javax.inject.Inject

@PerScreen
class BottomNavigationPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: BottomNavigationBindModel,
        private val screenNavigationProvider: ScreenScopeNavigationProvider,
        override val commandExecutor: NavigationCommandExecutor
) : BaseRxPresenter(basePresenterDependency), CommandExecutionPresenter {

    override fun onFirstLoad() {
        openTab(BottomTabType.HOME)
        bm.backPressed bindTo ::onBackPressed
        bm.bottomNavClicked bindTo ::openTab
    }

    override fun onLoad(viewRecreated: Boolean) {
        getTabFragmentNavigator().setActiveTabReopenedListener {
            RemoveAll(isTab = true).execute()
        }
    }

    private fun openTab(type: BottomTabType) {
        val route: FragmentRoute = when (type) {
            BottomTabType.HOME -> HomeFragmentRoute()
            else -> TODO()
        }
        Replace(route).execute()
    }

    private fun onBackPressed() {
        when {
            hasTabsInStack() -> RemoveLast(isTab = true).execute()
            else -> FinishAffinity().execute()
        }
    }

    private fun hasTabsInStack(): Boolean {
        return getTabFragmentNavigator().backStackEntryCount > 1
    }

    private fun getTabFragmentNavigator(): TabFragmentNavigatorInterface =
            screenNavigationProvider.getFragmentNavigationHolder().tabFragmentNavigator
}