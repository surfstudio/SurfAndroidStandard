package ru.surfstudio.standard.ui.screen.main

import javax.inject.Inject

import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}