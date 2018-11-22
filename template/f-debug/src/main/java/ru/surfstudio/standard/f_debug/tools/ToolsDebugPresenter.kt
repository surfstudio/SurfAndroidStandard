package ru.surfstudio.standard.f_debug.tools

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.DebugInteractor
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootDebugActivityRoute
import javax.inject.Inject

/**
 * Презентер экрана показа Tools
 */
@PerScreen
class ToolsDebugPresenter @Inject constructor(
        private val debugInteractor: DebugInteractor,
        private val activityNavigator: ActivityNavigator,
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<ToolsDebugActivityView>(basePresenterDependency) {
    private val sm = ToolsDebugScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        sm.isStethoEnabled = debugInteractor.isStethoEnabled
        view.render(sm)
    }

    fun setStethoEnable(enable: Boolean) {
        if (sm.isStethoEnabled != enable) {
            debugInteractor.isStethoEnabled = enable
            activityNavigator.start(RebootDebugActivityRoute())
        }
    }
}
