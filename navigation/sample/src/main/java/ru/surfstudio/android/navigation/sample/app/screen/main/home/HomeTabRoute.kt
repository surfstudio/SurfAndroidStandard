package ru.surfstudio.android.navigation.sample.app.screen.main.home

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute

class HomeTabRoute : FragmentRoute(), TabHeadRoute {
    override fun getScreenClass(): Class<out Fragment>? = HomeTabFragment::class.java
}