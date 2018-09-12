package ru.surfstudio.android.security.sample.ui.screen.session

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана сессии
 */
class SessionActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, SessionActivityView::class.java)
    }
}
