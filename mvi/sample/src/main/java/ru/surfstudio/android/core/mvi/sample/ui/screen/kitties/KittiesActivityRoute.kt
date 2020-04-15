package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

internal class KittiesActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, KittiesActivityView::class.java)
}