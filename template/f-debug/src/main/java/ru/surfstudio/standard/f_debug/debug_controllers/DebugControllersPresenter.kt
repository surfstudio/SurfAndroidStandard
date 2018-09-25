package ru.surfstudio.standard.f_debug.debug_controllers

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана для показа контроллеров, используемых в приложении
 */
@PerScreen
class DebugControllersPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<DebugControllersActivityView>(basePresenterDependency) {

    private val screenModel = DebugControllersScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}
