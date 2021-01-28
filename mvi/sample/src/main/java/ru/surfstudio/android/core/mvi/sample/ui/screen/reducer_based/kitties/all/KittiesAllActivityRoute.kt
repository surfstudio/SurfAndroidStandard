package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.all

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

internal class KittiesAllActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, KittiesAllActivityView::class.java)
}