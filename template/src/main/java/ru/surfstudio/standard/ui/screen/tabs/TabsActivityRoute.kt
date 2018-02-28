package ru.surfstudio.standard.ui.screen.tabs

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Created by azaytsev on 27.02.18.
 */
class TabsActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent = Intent(context, TabsActivityView::class.java)
}