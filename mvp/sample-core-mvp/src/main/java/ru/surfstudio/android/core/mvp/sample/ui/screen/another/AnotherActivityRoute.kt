package ru.surfstudio.android.core.mvp.sample.ui.screen.another

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class AnotherActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, AnotherActivityView::class.java)
    }
}