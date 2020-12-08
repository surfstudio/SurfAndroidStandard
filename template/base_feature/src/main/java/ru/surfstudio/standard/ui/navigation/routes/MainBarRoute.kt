package ru.surfstudio.standard.ui.navigation.routes

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Роут главного экрана с табами
 */
class MainBarRoute : FragmentRoute() {

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.standard.f_main.bar.MainBarFragmentView"
    }
}
