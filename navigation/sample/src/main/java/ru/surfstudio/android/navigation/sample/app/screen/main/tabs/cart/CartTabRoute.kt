package ru.surfstudio.android.navigation.sample.app.screen.main.tabs.cart

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabRootRoute

class CartTabRoute : FragmentRoute(), TabRootRoute {
    override fun getScreenClass(): Class<out Fragment>? = CartTabFragment::class.java
}