package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.profile

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabRootRoute

class ProfileRoute : FragmentRoute(), TabRootRoute {
    override fun getScreenClass(): Class<out Fragment>? = ProfileFragmentView::class.java
}