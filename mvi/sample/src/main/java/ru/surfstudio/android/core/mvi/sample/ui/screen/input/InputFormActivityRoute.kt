package ru.surfstudio.android.core.mvi.sample.ui.screen.input

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute

/**
 * Роут [InputFormActivityView]
 */
class InputFormActivityRoute : ActivityWithResultRoute<String>() {

    override fun prepareIntent(context: Context): Intent =
            Intent(context, InputFormActivityView::class.java)

}