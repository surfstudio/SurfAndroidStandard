package ru.surfstudio.standard.f_main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.navigation.FeedFragmentRoute
import ru.surfstudio.standard.ui.navigation.MainTabType
import ru.surfstudio.standard.ui.navigation.ProfileFragmentRoute
import ru.surfstudio.standard.ui.navigation.SearchFragmentRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val tabNavigator: TabFragmentNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm = MainScreenModel()

    override fun onFirstLoad() {
        view.render(sm)
        openFragment(sm.tabType)
    }

    fun onTabClick(tabType: MainTabType) {
        sm.tabType = tabType
        view.render(sm)
        openFragment(sm.tabType)
    }

    private fun openFragment(tabType: MainTabType) {
        val tabRoute: FragmentRoute = when (tabType) {
            MainTabType.FEED -> FeedFragmentRoute()
            MainTabType.SEARCH -> SearchFragmentRoute()
            MainTabType.PROFILE -> ProfileFragmentRoute()
        }
        tabNavigator.open(tabRoute)
    }
}