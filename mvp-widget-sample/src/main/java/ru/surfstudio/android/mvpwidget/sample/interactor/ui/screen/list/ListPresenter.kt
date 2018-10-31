package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана todo
 */
@PerScreen
class ListPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<ListActivityView>(basePresenterDependency) {

    private val screenModel = ListScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}
