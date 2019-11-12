package ru.surfstudio.android.core.ui.sample.ui.screen.result

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultNoDataRoute

/**
 * Роутер открытия главного экрана
 */
class ResultActivityRoute : ActivityWithResultNoDataRoute() {

    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, ResultActivityView::class.java)
    }
}