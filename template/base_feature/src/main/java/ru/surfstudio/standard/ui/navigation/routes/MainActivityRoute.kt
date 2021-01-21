package ru.surfstudio.standard.ui.navigation.routes

import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 * Роут главного экрана
 */
class MainActivityRoute : ActivityRoute() {

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.standard.f_main.MainActivityView"
    }
}