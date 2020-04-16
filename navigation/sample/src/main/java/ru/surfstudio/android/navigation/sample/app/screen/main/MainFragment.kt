package ru.surfstudio.android.navigation.sample.app.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import ru.surfstudio.android.navigation.command.activity.Finish
import ru.surfstudio.android.navigation.command.fragment.RemoveAll
import ru.surfstudio.android.navigation.command.fragment.RemoveLast
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.supplier.container.FragmentContainer
import ru.surfstudio.android.navigation.supplier.holder.FragmentNavigationHolder
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.main.MainTabType.*
import ru.surfstudio.android.navigation.sample.app.screen.main.tabs.cart.CartTabRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.tabs.home.HomeTabRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.tabs.profile.ProfileTabRoute
import ru.surfstudio.android.navigation.sample.app.utils.addOnBackPressedListener

class MainFragment : Fragment(), FragmentContainer {

    override val containerId: Int = R.id.main_fragment_container

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addOnBackPressedListener {
            when {
                hasTabsInStack() -> App.navigator.execute(RemoveLast(sourceTag = tag!!, isTab = true))
                else -> App.navigator.execute(Finish())
            }
        }

        setActiveTabReopenedListener { App.navigator.execute(RemoveAll(sourceTag = tag!!, isTab = true)) }

        if (savedInstanceState == null) {
            navigateToTab(HOME)
        }

        home_tab_btn.setOnClickListener { navigateToTab(HOME) }
        cart_tab_btn.setOnClickListener { navigateToTab(CART) }
        profile_tab_btn.setOnClickListener { navigateToTab(PROFILE) }
    }

    private fun setActiveTabReopenedListener(listener: (String) -> Unit) {
        getFragmentHolder()
                .tabFragmentNavigator
                .setActiveTabReopenedListener(listener)
    }

    private fun navigateToTab(type: MainTabType) {
        val route: FragmentRoute = when (type) {
            HOME -> HomeTabRoute()
            CART -> CartTabRoute()
            PROFILE -> ProfileTabRoute()
        }
        App.navigator.execute(Replace(route, sourceTag = tag!!))
    }

    private fun hasTabsInStack(): Boolean {
        val backStackCount = getFragmentHolder()
                .tabFragmentNavigator
                .backStackEntryCount
        return backStackCount > 1
    }

    private fun getFragmentHolder(): FragmentNavigationHolder {
        return App.supplier
                .obtain()
                .fragmentSupplier
                .obtain(tag!!)
    }
}