package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child2

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class Child2TabFragmentPresenter @Inject constructor(
        private val route: Child2TabFragmentRoute,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<Child2TabFragmentView>(basePresenterDependency) {

    val screenModel: Child2TabFragmentScreenModel = Child2TabFragmentScreenModel(route.id)

    override fun onResume() {
        super.onResume()

        view.render(screenModel)
    }
}