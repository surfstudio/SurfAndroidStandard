package ru.surfstudio.core_mvp_rxbinding_sample

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class MainActivityRoute: ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent = Intent(context, MainActivityView::class.java)
}