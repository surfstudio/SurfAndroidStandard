package ru.surfstudio.standard.ui.screen.main

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityRoute

/**
 * Роутер открытия главного экрана
 */
class MainActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, MainActivityView::class.java)
    }

}