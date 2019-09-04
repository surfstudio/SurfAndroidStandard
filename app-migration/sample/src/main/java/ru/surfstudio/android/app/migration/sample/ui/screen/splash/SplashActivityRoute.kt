package ru.surfstudio.android.app.migration.sample.ui.screen.splash

import android.content.Context
import android.content.Intent

import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роутер открытия загрузочного экрана приложения
 */
class SplashActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, SplashActivityView::class.java)
    }
}
