package ru.surfstudio.android.navigation.sample.app.screen.main.profile

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabRootRoute

class ProfileTabRoute : FragmentRoute(), TabRootRoute {
    override fun getScreenClass(): Class<out Fragment>? = ProfileTabFragment::class.java
}