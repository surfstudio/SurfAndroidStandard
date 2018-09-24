package ru.surfstudio.standard.f_debug

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.fcm.DebugFcmActivityRoute
import javax.inject.Inject

/**
 * Презентер экрана показа информации для дебага
 */
@PerScreen
class DebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<DebugActivityView>(basePresenterDependency) {

    private val screenModel = DebugScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun openControllersScreen() {

    }

    fun openFcmTokenScreen() {
        activityNavigator.start(DebugFcmActivityRoute())
    }
}
