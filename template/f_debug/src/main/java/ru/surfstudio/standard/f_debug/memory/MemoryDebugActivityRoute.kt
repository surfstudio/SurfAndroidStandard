package ru.surfstudio.standard.f_debug.memory

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана Memory
 */
class MemoryDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, MemoryDebugActivityView::class.java)
    }
}