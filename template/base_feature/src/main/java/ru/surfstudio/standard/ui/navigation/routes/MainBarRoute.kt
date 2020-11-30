package ru.surfstudio.standard.ui.navigation.routes

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Роут экрана входа в аккаунт по логину и паролю
 */
class MainBarRoute : FragmentRoute() {

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.standard.f_main.bar.MainBarFragmentView"
    }
}
