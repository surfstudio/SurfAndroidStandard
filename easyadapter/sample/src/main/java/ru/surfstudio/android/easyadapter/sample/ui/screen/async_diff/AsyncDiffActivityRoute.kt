package ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class AsyncDiffActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context?) =
            Intent(context, AsyncDiffActivityView::class.java)
}