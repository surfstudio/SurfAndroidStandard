package ru.surfstudio.standard.f_debug.memory.route

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.template.f_debug.R

class StorageDebugScreenRoute : ActivityRoute() {
    override fun prepareIntent(context: Context) =
            Intent(context.resources.getString(R.string.impl_intent_debug_storage))
}