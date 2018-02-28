package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child4

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class Child4TabFragmentPresenter @Inject constructor(
        private val route: Child4TabFragmentRoute,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<Child4TabFragmentView>(basePresenterDependency) {

    val screenModel: Child4TabFragmentScreenModel = Child4TabFragmentScreenModel(route.id)

    override fun onResume() {
        super.onResume()

        view.render(screenModel)
    }
}