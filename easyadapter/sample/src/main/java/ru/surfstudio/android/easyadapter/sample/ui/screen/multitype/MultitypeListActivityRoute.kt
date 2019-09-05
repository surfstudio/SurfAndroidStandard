package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class MultitypeListActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, MultitypeListActivityView::class.java)
    }
}