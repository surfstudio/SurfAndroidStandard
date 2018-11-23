package ru.surfstudio.standard.f_debug.ui_tools

import android.content.Context
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.utilktx.util.SdkUtils
import javax.inject.Inject
import android.provider.Settings
import io.reactivex.Observable

class OverlayPermissionChecker @Inject constructor(
        private val context: Context,
        private val activityNavigator: ActivityNavigator
) {
    fun checkOverlayPermission(): Observable<Boolean> {
        return if (SdkUtils.isAtLeastMarshmallow() && !Settings.canDrawOverlays(context)) {
            val result = activityNavigator.observeResult<Boolean>(UiToolsCheckOverlayPermissionRoute::class.java)
                    .map { Settings.canDrawOverlays(context) }

            activityNavigator.startForResult(UiToolsCheckOverlayPermissionRoute())

            result
        } else {
            Observable.just(true)
        }
    }
}