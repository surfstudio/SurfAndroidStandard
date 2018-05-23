package ru.surfstudio.android.rx.downloader.sample.ui.screen.main

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class MainActivityRoute : ActivityRoute() {

    override fun prepareIntent(context: Context): Intent {
        val intent = Intent(context, MainActivityView::class.java)
        return intent
    }
}
