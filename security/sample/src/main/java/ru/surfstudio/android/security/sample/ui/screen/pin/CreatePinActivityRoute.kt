package ru.surfstudio.android.security.sample.ui.screen.pin

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsRoute
import ru.surfstudio.android.security.sample.domain.ApiKey

/**
 * Роут экрана ввода pin-кода
 */
class CreatePinActivityRoute(val apiKey: ApiKey) : ActivityWithParamsRoute() {

    constructor(intent: Intent) : this(intent.getSerializableExtra(Route.EXTRA_FIRST) as ApiKey)

    override fun prepareIntent(context: Context): Intent {
        return Intent(context, CreatePinActivityView::class.java).apply {
            putExtra(Route.EXTRA_FIRST, apiKey)
        }
    }
}
