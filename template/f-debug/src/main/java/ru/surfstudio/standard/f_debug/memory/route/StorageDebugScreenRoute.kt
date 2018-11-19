package ru.surfstudio.standard.f_debug.memory.route

import android.content.Context
import android.content.Intent
import com.krishna.debug_tools.activity.ActivityDebugTools
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class StorageDebugScreenRoute : ActivityRoute() {
    override fun prepareIntent(context: Context) = Intent(context, ActivityDebugTools::class.java)
}
