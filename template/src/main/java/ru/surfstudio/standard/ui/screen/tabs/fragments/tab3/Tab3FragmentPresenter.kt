package ru.surfstudio.standard.ui.screen.tabs.fragments.tab3

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.ui.screen.tabs.fragments.child.child3.Child3TabFragmentRoute
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class Tab3FragmentPresenter @Inject constructor(
        private val tabFragmentNavigator: TabFragmentNavigator,

        private val route: Tab3FragmentRoute,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<Tab3FragmentView>(basePresenterDependency) {

    init {
        Logger.d("2222 Tab route id = ${route.id}")
    }

    val screenModel: Tab3FragmentScreenModel = Tab3FragmentScreenModel(route.id)

    override fun onResume() {
        super.onResume()
        view.render(screenModel)
    }

    fun openTab() {
        tabFragmentNavigator.open(Child3TabFragmentRoute(screenModel.id + 1))
    }
}