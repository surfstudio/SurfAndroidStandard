package ru.surfstudio.android.core.mvp.rx.sample

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class MainActivityRoute: ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent = Intent(context, MainActivityView::class.java)
}