package ru.surfstudio.android.firebase.sample.ui.screen.push.data

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsRoute
import ru.surfstudio.android.firebase.sample.domain.notification.Notification

class DataPushActivityRoute(val notification: Notification?) : ActivityWithParamsRoute() {

    constructor(intent: Intent) : this(intent.getSerializableExtra(Route.EXTRA_FIRST) as Notification?)

    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, DataPushActivityView::class.java).apply {
            putExtra(Route.EXTRA_FIRST, notification)
        }
    }
}