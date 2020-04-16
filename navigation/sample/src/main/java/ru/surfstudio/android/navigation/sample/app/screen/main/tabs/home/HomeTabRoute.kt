package ru.surfstudio.android.navigation.sample.app.screen.main.tabs.home

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabRootRoute

class HomeTabRoute : FragmentRoute(), TabRootRoute {
    override fun getScreenClass(): Class<out Fragment>? = HomeTabFragment::class.java
}