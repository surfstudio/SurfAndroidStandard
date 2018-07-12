package ru.surfstudio.standard.ui.screen.webview

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsRoute

/**
 * Route экрана с вебвью, принимает заголовок для тулбара
 */
class WebViewRoute(val title: String) : ActivityWithParamsRoute() {

    constructor(intent: Intent) : this(
            title = intent.getStringExtra(Route.EXTRA_FIRST))

    override fun prepareIntent(context: Context): Intent {
        val intent = Intent(context, WebViewActivityView::class.java)
        intent.putExtra(Route.EXTRA_FIRST, title)
        return intent
    }
}