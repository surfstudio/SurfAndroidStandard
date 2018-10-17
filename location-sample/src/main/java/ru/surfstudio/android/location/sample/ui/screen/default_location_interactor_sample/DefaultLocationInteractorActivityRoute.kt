package ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Маршрут экрана [DefaultLocationInteractorActivityView]
 */
class DefaultLocationInteractorActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context?) =
            Intent(context, DefaultLocationInteractorActivityView::class.java)
}