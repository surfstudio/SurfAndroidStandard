package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана [ComplexListActivityView]
 */
class ComplexListActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent = Intent(context, ComplexListActivityView::class.java)
}