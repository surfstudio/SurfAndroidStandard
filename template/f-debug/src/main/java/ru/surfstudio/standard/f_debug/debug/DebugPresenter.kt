package ru.surfstudio.standard.f_debug.debug

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.common_controllers.CommonControllersDebugActivityRoute
import ru.surfstudio.standard.f_debug.fcm.FcmDebugActivityRoute
import javax.inject.Inject

/**
 * Презентер экрана показа информации для дебага
 */
@PerScreen
class DebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<DebugActivityView>(basePresenterDependency) {

    fun openControllersScreen() {
        activityNavigator.start(CommonControllersDebugActivityRoute())
    }

    fun openFcmTokenScreen() {
        activityNavigator.start(FcmDebugActivityRoute())
    }
}
