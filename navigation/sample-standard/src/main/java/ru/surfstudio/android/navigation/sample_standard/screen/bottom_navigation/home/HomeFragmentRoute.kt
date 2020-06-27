package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.home

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class HomeFragmentRoute: FragmentRoute() {

    override fun getScreenClass(): Class<out Fragment>? = HomeFragment::class.java
}