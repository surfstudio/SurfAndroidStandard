package ru.surfstudio.android.loadstate.sample.ui.screen.actions

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsAndResultRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentWithParamsRoute

/**
 * Роут экрана todo
 */
class RendererWithActionsDemoActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        val intent = Intent(context, RendererWithActionsDemoActivityView::class.java)
        return intent
    }
}
