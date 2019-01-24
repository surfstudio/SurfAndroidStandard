package ru.surfstudio.standard.f_debug.debug

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.reused_components.ReusedComponentsDebugActivityRoute
import ru.surfstudio.standard.f_debug.developer_tools.DeveloperToolsDebugActivityRoute
import ru.surfstudio.standard.f_debug.fcm.FcmDebugActivityRoute
import ru.surfstudio.standard.f_debug.info.AppInfoDebugActivityRoute
import ru.surfstudio.standard.f_debug.memory.MemoryDebugActivityRoute
import ru.surfstudio.standard.f_debug.server_settings.ServerSettingsDebugActivityRoute
import ru.surfstudio.standard.f_debug.ui_tools.UiToolsDebugActivityRoute
import ru.surfstudio.standard.f_debug.tools.ToolsDebugActivityRoute
import javax.inject.Inject

/**
 * Презентер экрана показа информации для дебага
 */
@PerScreen
class DebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<DebugActivityView>(basePresenterDependency) {

    fun openServerSettingsScreen() {
        activityNavigator.start(ServerSettingsDebugActivityRoute())
    }

    fun openReusedComponentsScreen() {
        activityNavigator.start(ReusedComponentsDebugActivityRoute())
    }

    fun openFcmTokenScreen() {
        activityNavigator.start(FcmDebugActivityRoute())
    }

    fun openMemoryScreen() {
        activityNavigator.start(MemoryDebugActivityRoute())
    }

    fun openAppInfoScreen() {
        activityNavigator.start(AppInfoDebugActivityRoute())
    }

    fun openUiToolsScreen() {
        activityNavigator.start(UiToolsDebugActivityRoute())
    }

    fun openDeveloperToolsScreen() {
        activityNavigator.start(DeveloperToolsDebugActivityRoute())
    }

    fun openToolsScreen() {
        activityNavigator.start(ToolsDebugActivityRoute())
    }
}
