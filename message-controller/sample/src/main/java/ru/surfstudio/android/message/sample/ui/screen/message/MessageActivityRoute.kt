package ru.surfstudio.android.message.sample.ui.screen.message

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class MessageActivityRoute(
        val infiniteLoop: Boolean = true
) : ActivityRoute() {

    constructor(intent: Intent): this(intent.getBooleanExtra(EXTRA_FIRST, true))

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, MessageActivityView::class.java)
                    .putExtra(EXTRA_FIRST, infiniteLoop)
}