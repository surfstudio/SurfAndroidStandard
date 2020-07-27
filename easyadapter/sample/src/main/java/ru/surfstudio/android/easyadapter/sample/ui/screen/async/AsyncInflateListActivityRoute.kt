package ru.surfstudio.android.easyadapter.sample.ui.screen.async

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class AsyncInflateListActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?) =
            Intent(context, AsyncInflateListActivityView::class.java)
}