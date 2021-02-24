package ru.surfstudio.android.navigation.sample.app.screen.number

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.route.activity.getDataBundle

class NumberRoute(val number: Int): ActivityRoute() {

    constructor(intent: Intent) : this(intent.getDataBundle()?.getInt(Route.EXTRA_FIRST) ?: 0)

    override fun getId(): String {
        return super.getId() + number
    }

    override fun prepareData(): Bundle {
        return bundleOf(Route.EXTRA_FIRST to number)
    }

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.android.navigation.sample.app.screen.number.NumberActivity"
    }
}