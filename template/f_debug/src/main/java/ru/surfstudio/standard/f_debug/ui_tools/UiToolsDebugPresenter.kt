package ru.surfstudio.standard.f_debug.ui_tools

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.DebugInteractor
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootDebugActivityRoute
import ru.surfstudio.standard.f_debug.window_vqa.WindowVQADebugActivityRoute
import javax.inject.Inject

/**
 * Презентер экрана показа UI-tools
 */
@PerScreen
class UiToolsDebugPresenter @Inject constructor(
        private val interactor: DebugInteractor,
        private val activityNavigator: ActivityNavigator,
        private val debugOverlayPermissionChecker: DebugOverlayPermissionChecker,
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<UiToolsDebugActivityView>(basePresenterDependency) {

    private val sm = UiToolsDebugScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        sm.isFpsEnabled = interactor.isFpsEnabled
        view.render(sm)
    }

    fun setFpsEnable(enable: Boolean) {
        if (sm.isFpsEnabled != enable) {
            subscribe(debugOverlayPermissionChecker.checkOverlayPermission()) {
                if (it) {
                    interactor.isFpsEnabled = enable
                    sm.isFpsEnabled = enable
                    view.render(sm)
                    activityNavigator.start(RebootDebugActivityRoute())
                } else {
                    view.render(sm)
                }
            }
        }
    }

    fun openWindowVQA() {
        activityNavigator.start(WindowVQADebugActivityRoute())
    }
}
