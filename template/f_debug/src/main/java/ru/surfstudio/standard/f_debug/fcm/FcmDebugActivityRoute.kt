package ru.surfstudio.standard.f_debug.fcm

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана показа fcm-токена
 */
class FcmDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, FcmDebugActivityView::class.java)
    }
}
