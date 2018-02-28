package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child3

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class Child3TabFragmentPresenter @Inject constructor(
        private val route: Child3TabFragmentRoute,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<Child3TabFragmentView>(basePresenterDependency) {

    val screenModel: Child3TabFragmentScreenModel = Child3TabFragmentScreenModel(route.id)

    override fun onResume() {
        super.onResume()

        view.render(screenModel)
    }
}