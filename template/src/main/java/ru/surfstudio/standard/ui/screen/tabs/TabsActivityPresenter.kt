package ru.surfstudio.standard.ui.screen.tabs

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.ui.screen.tabs.fragments.tab1.Tab1FragmentRoute
import ru.surfstudio.standard.ui.screen.tabs.fragments.tab2.Tab2FragmentRoute
import ru.surfstudio.standard.ui.screen.tabs.fragments.tab3.Tab3FragmentRoute
import ru.surfstudio.standard.ui.screen.tabs.fragments.tab4.Tab4FragmentRoute
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class TabsActivityPresenter @Inject constructor(
        private val tabsFragmentNavigator: TabFragmentNavigator,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<TabsActivityView>(basePresenterDependency) {

    var activeTab = 0

    fun openTab(tab: Int) {
        Logger.d("2222 active tab $activeTab | open tab $tab")

        if (tab == activeTab) {
            //tabsFragmentNavigator.open(ChildTabFragmentRoute(tab))
        } else {
            tabsFragmentNavigator.open(nextRoute(tab))
            activeTab = tab
        }
    }

    private fun nextRoute(tab: Int): FragmentRoute {
        return when (tab) {
            1 -> Tab1FragmentRoute(tab)
            2 -> Tab2FragmentRoute(tab)
            3 -> Tab3FragmentRoute(tab)
            4 -> Tab4FragmentRoute(tab)
            else -> {
                Tab1FragmentRoute(tab)
            }
        }
    }
}