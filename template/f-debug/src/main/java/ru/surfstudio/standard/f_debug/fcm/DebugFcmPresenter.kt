package ru.surfstudio.standard.f_debug.fcm

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана показа fcm-токена
 */
@PerScreen
class DebugFcmPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<DebugFcmActivityView>(basePresenterDependency) {

    private val screenModel = DebugFcmScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}
