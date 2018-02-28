package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child1

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
@PerScreen
class ChildTabFragmentPresenter @Inject constructor(
        private val route: ChildTabFragmentRoute,
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<ChildTabFragmentView>(basePresenterDependency) {

    val screenModel: ChildTabFragmentScreenModel = ChildTabFragmentScreenModel(route.id)

    override fun onResume() {
        super.onResume()

        view.render(screenModel)
    }
}