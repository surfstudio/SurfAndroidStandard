package ru.surfstudio.standard.ui.screen.tabs.fragments.tab1

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.ui.screen.tabs.fragments.child.child1.ChildTabFragmentRoute
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class Tab1FragmentPresenter @Inject constructor(
        private val tabFragmentNavigator: TabFragmentNavigator,
        private val route: Tab1FragmentRoute,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<Tab1FragmentView>(basePresenterDependency) {

    init {
        Logger.d("2222 Tab1 route id = ${route.id}")
    }

    val screenModel: Tab1FragmentScreenModel = Tab1FragmentScreenModel(route.id)

    override fun onResume() {
        super.onResume()
        view.render(screenModel)
    }

    fun openTab() {
        Logger.d("2222 new id ${screenModel.id + 1}")
        tabFragmentNavigator.open(ChildTabFragmentRoute(screenModel.id + 1))
    }
}