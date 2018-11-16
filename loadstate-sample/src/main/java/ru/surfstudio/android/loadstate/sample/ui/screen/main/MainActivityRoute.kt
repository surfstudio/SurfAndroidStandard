package ru.surfstudio.android.loadstate.sample.ui.screen.main

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана todo
 */
class MainActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        val intent = Intent(context, MainActivityView::class.java)
        return intent
    }
}
