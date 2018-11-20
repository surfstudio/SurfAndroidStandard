package ru.surfstudio.standard.f_debug.ui_tools

import android.os.Build
import androidx.annotation.RequiresApi
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootDebugActivityRoute
import ru.surfstudio.standard.i_debug.DebugInteractor
import javax.inject.Inject

/**
 * Презентер экрана показа UI-tools
 */
@PerScreen
class UiToolsDebugPresenter @Inject constructor(
        private val interactor: DebugInteractor,
        private val activityNavigator: ActivityNavigator,
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
            interactor.isFpsEnabled = enable
            activityNavigator.start(RebootDebugActivityRoute())
            activityNavigator.finishAffinity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkOverlayPermission() {
        activityNavigator.start(UiToolsCheckOverlayPermissionRoute())
    }
}
