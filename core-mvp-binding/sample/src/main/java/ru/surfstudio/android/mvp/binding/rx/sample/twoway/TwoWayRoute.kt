
package ru.surfstudio.android.mvp.binding.rx.sample.twoway

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Маршрут [TwoWayActivityView].
 */
class TwoWayRoute : ActivityRoute() {

    override fun prepareIntent(context: Context) = Intent(context, TwoWayActivityView::class.java).apply {
    }
}