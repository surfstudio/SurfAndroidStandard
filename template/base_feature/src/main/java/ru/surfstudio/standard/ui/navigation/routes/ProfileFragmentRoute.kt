package ru.surfstudio.standard.ui.navigation.routes

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute

class ProfileFragmentRoute : FragmentRoute(), TabHeadRoute {

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.standard.f_profile.ProfileFragmentView"
    }
}