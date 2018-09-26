package ru.surfstudio.android.custom_scope_sample.ui.screen.other

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class OtherActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, OtherActivityView::class.java)
    }
}