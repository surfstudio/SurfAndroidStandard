package ru.surfstudio.standard.ui.screen.splash

import android.content.Context
import android.content.Intent

import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityRoute


/**
 * Маршрут сплеша
 */
class SplashRoute : ActivityRoute() {

    override fun prepareIntent(context: Context): Intent {
        return Intent(context, SplashActivityView::class.java)
    }
}
