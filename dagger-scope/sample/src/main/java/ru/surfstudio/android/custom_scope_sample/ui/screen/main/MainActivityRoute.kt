package ru.surfstudio.android.custom_scope_sample.ui.screen.main

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роутер открытия главного экрана
 */
class MainActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, MainActivityView::class.java)
    }
}