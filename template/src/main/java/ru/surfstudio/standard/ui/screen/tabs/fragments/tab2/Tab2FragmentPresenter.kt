package ru.surfstudio.standard.ui.screen.tabs.fragments.tab2

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.ui.screen.tabs.fragments.child.child1.ChildTabFragmentRoute
import ru.surfstudio.standard.ui.screen.tabs.fragments.child.child2.Child2TabFragmentRoute
import ru.surfstudio.standard.ui.screen.tabs.fragments.tab1.Tab1FragmentRoute
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class Tab2FragmentPresenter @Inject constructor(
        private val tabFragmentNavigator: TabFragmentNavigator,
        private val route: Tab2FragmentRoute,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<Tab2FragmentView>(basePresenterDependency) {

    init {
        Logger.d("2222 Tab route id = ${route.id}")
    }

    val screenModel: Tab2FragmentScreenModel = Tab2FragmentScreenModel(route.id)

    override fun onResume() {
        super.onResume()
        view.render(screenModel)
    }

    fun openTab() {
        tabFragmentNavigator.showAtTab(Tab1FragmentRoute(1),ChildTabFragmentRoute(1))
    }
}