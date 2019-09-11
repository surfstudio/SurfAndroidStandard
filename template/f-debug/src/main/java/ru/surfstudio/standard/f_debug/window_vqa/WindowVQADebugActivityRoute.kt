package ru.surfstudio.standard.f_debug.window_vqa

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

private const val WINDOW_VQA_PACKAGE_NAME = "com.dziemia.w.window"

class WindowVQADebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return context.packageManager.getLaunchIntentForPackage(WINDOW_VQA_PACKAGE_NAME)
            ?: Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$WINDOW_VQA_PACKAGE_NAME"))
    }
}