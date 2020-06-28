package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.home

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabRootRoute

class HomeFragmentRoute: FragmentRoute(), TabRootRoute {

    override fun getScreenClass(): Class<out Fragment>? = HomeFragment::class.java
}