package ru.surfstudio.android.loadstate.sample.ui.screen.stubs

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана для демонстрации работы DefaultLoadStateRenderer с использованием заглушек (шиммеров)
 */
class RendererWithStubsDemoActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, RendererWithStubsDemoActivityView::class.java)
    }
}
