package ru.surfstudio.standard.ui.navigation

import ru.surfstudio.android.core.ui.navigation.feature.route.feature.ActivityCrossFeatureRoute

/**
 * Роут главного экрана
 */
class MainActivityRoute : ActivityCrossFeatureRoute() {

    override fun targetClassPath(): String {
        return "ru.surfstudio.standard.f_main.MainActivityView"
    }
}