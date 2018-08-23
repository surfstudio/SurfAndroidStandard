package ru.surfstudio.android.firebase.sample.ui.screen.push

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class PushActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, PushActivityView::class.java)
    }
}