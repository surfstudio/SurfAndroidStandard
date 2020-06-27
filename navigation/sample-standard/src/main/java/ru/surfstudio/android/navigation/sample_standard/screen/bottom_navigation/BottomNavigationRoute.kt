package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class BottomNavigationRoute : FragmentRoute() {

    override fun getScreenClass(): Class<out Fragment>? = BottomNavigationFragmentView::class.java
}