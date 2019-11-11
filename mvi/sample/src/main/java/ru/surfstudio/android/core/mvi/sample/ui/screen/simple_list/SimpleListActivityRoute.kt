package ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute


/**
 * Роут экрана [SimpleListActivityView]
 */
class SimpleListActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, SimpleListActivityView::class.java)
}