package ru.surfstudio.android.core.ui.sample.ui.screen.result

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultNoDataRoute

/**
 * Роу открытия [ResultNoDataActivityView]
 */
class ResultNoDataActivityRoute : ActivityWithResultNoDataRoute() {

    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, ResultNoDataActivityView::class.java)
    }
}