package ru.surfstudio.standard.f_main

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute


class MainActivityRoute : ActivityRoute() {


    override fun prepareIntent(context: Context): Intent {
        val intent = Intent(context, MainActivityView::class.java)
        return intent
    }
}
