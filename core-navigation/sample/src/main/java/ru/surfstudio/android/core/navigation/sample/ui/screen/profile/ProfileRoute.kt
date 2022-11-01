package ru.surfstudio.android.core.navigation.sample.ui.screen.profile

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsRoute
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Роут экрана профиля
 */
class ProfileRoute(val userName: String) : ActivityWithParamsRoute() {

    constructor(intent: Intent) : this(intent.getStringExtra(Route.EXTRA_FIRST) ?: EMPTY_STRING)

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, ProfileActivityView::class.java).apply {
                putExtra(Route.EXTRA_FIRST, userName)
            }

}