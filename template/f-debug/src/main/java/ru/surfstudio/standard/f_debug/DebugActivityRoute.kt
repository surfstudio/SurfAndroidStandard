package ru.surfstudio.standard.f_debug

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.template.f_debug.BuildConfig
import ru.surfstudio.standard.f_main.MainActivityView

/**
 * Роут экрана показа информации для дебага
 */
class DebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context,
                if (BuildConfig.BUILD_TYPE == "debug" || BuildConfig.BUILD_TYPE == "qa")
                    DebugActivityView::class.java
                else
                    MainActivityView::class.java)
    }
}
