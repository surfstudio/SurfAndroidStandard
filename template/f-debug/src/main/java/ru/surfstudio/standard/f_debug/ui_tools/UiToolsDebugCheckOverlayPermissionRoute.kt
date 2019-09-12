package ru.surfstudio.standard.f_debug.ui_tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute

/**
 * Роут экрана разрешения наложения поверз других окон
 */
@RequiresApi(Build.VERSION_CODES.M)
class UiToolsDebugCheckOverlayPermissionRoute : ActivityWithResultRoute<Boolean>() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
        )
    }
}