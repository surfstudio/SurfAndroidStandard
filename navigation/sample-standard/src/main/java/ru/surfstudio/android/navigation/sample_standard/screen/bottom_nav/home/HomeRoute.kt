package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.home

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabRootRoute

class HomeRoute: FragmentRoute(), TabRootRoute {

    override fun getScreenClass(): Class<out Fragment>? = HomeFragmentView::class.java
}