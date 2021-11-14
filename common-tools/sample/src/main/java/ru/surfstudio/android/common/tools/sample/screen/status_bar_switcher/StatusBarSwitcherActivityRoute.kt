package ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class StatusBarSwitcherActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context) =
            Intent(context, StatusBarSwitcherActivityView::class.java)
}