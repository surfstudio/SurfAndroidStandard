package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute

/**
 * Роут экрана todo
 */
class ListActivityRoute : ActivityWithResultRoute<String>() {
    override fun prepareIntent(context: Context): Intent {
        val intent = Intent(context, ListActivityView::class.java)
        return intent
    }
}
