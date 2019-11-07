package ru.surfstudio.android.core.ui.sample.ui.screen.message

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.sample.ui.screen.main.MainActivityView

class MessageActivityRoute(
        val infiniteLoop: Boolean = true
) : ActivityRoute() {

    constructor(intent: Intent): this(intent.getBooleanExtra(Route.EXTRA_FIRST, true))

    override fun prepareIntent(context: Context?) =
            Intent(context, MainActivityView::class.java)
                    .putExtra(Route.EXTRA_FIRST, infiniteLoop)
}