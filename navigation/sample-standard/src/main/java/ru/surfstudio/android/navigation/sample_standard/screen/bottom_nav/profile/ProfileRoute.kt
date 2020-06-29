package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.profile

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute

class ProfileRoute : FragmentRoute(), TabHeadRoute {
    override fun getScreenClass(): Class<out Fragment>? = ProfileFragmentView::class.java
}