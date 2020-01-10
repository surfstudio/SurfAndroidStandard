package ru.surfstudio.android.location.sample.ui.screen.location_service_sample

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Маршрут экрана [LocationServiceActivityView]
 */
class LocationServiceActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context?) = Intent(context, LocationServiceActivityView::class.java)
}