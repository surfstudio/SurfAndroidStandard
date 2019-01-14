package ru.surfstudio.android.core.mvp.rx.sample.checkbox

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class CheckboxActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, CheckboxActivityView::class.java)
}