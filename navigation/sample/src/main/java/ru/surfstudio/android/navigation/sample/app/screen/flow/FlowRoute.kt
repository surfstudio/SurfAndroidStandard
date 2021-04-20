package ru.surfstudio.android.navigation.sample.app.screen.flow

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.route.activity.getDataBundle

class FlowRoute(val screenId: String): ActivityRoute() {

    constructor(intent: Intent) : this(intent.getDataBundle()?.getString(Route.EXTRA_FIRST) ?: "")

    override fun getId(): String {
        return super.getId() + screenId
    }

    override fun prepareData(): Bundle {
        return bundleOf(Route.EXTRA_FIRST to screenId)
    }

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.android.navigation.sample.app.screen.flow.FlowActivity"
    }
}