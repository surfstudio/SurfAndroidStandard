package ru.surfstudio.standard.ui.screen.tabs.fragments.tab4

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.ui.screen.tabs.fragments.Tab4FragmentScreenModel
import ru.surfstudio.standard.ui.screen.tabs.fragments.child.ChildTabFragmentRoute
import ru.surfstudio.standard.ui.screen.tabs.fragments.tab1.Tab1FragmentRoute
import ru.surfstudio.standard.ui.screen.tabs.fragments.tab1.Tab1FragmentScreenModel
import ru.surfstudio.standard.ui.screen.tabs.fragments.tab1.Tab1FragmentView
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class Tab4FragmentPresenter @Inject constructor(
        private val tabFragmentNavigator: TabFragmentNavigator,
        private val route: Tab4FragmentRoute,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<Tab4FragmentView>(basePresenterDependency) {

    init {
        Logger.d("2222 Tab route id = ${route.id}")
    }

    val screenModel: Tab4FragmentScreenModel = Tab4FragmentScreenModel(route.id)

    override fun onResume() {
        super.onResume()
        view.render(screenModel)
    }

    fun openTab() {
        tabFragmentNavigator.show(ChildTabFragmentRoute(screenModel.id+1))
    }
}