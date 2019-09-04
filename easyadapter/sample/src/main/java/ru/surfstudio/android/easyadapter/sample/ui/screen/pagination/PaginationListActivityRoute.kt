package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class PaginationListActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, PaginationListActivityView::class.java)
    }
}