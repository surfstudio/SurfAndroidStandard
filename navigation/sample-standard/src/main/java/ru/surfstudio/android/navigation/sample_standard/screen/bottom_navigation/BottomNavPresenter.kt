package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.activity.FinishAffinity
import ru.surfstudio.android.navigation.command.fragment.RemoveAll
import ru.surfstudio.android.navigation.command.fragment.RemoveLast
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand.Companion.ACTIVITY_NAVIGATION_TAG
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.sample_standard.screen.base.presenter.CommandExecutionPresenter
import ru.surfstudio.android.navigation.scope.ScreenScopeNavigationProvider
import ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.home.HomeFragmentRoute
import ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.profile.ProfileRoute
import javax.inject.Inject

@PerScreen
class BottomNavPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: BottomNavBindModel,
        private val screenNavigationProvider: ScreenScopeNavigationProvider,
        override val commandExecutor: NavigationCommandExecutor
) : BaseRxPresenter(basePresenterDependency), CommandExecutionPresenter {

    override fun onFirstLoad() {
        openTab(BottomNavTabType.HOME)
        bm.backPressed bindTo ::onBackPressed
        bm.bottomNavClicked bindTo ::openTab
    }

    override fun onLoad(viewRecreated: Boolean) {
        getTabFragmentNavigator().setActiveTabReopenedListener {
            RemoveAll(isTab = true).execute()
        }
    }

    private fun openTab(type: BottomNavTabType) {
        val route: FragmentRoute = when (type) {
            BottomNavTabType.HOME -> HomeFragmentRoute()
            else -> ProfileRoute()
        }
        Replace(route).execute()
    }

    private fun onBackPressed() {
        when {
            hasTabsInStack() -> RemoveLast(isTab = true).execute()
            else -> RemoveLast(sourceTag = ACTIVITY_NAVIGATION_TAG).execute()

        }
    }

    private fun hasTabsInStack(): Boolean {
        return getTabFragmentNavigator().backStackEntryCount > 1
    }

    private fun getTabFragmentNavigator(): TabFragmentNavigatorInterface =
            screenNavigationProvider.getFragmentNavigationHolder().tabFragmentNavigator
}