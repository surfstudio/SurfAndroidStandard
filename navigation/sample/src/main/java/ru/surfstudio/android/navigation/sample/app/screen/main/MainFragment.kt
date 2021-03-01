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
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.provider.container.TabFragmentNavigationContainer
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.main.MainTabType.*
import ru.surfstudio.android.navigation.sample.app.screen.main.gallery.GalleryRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.home.HomeTabRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.profile.ProfileTabRoute
import ru.surfstudio.android.navigation.sample.app.utils.addOnBackPressedListener
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations

class MainFragment : Fragment(), TabFragmentNavigationContainer {

    override val containerId: Int = R.id.main_fragment_container

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initBackPressedListener()
        initActiveTabReopenedListener()

        if (savedInstanceState == null) {
            navigateToTab(HOME)
        }

        home_tab_btn.setOnClickListener { navigateToTab(HOME) }
        gallery_tab_btn.setOnClickListener { navigateToTab(GALLERY) }
        profile_tab_btn.setOnClickListener { navigateToTab(PROFILE) }
    }

    private fun initActiveTabReopenedListener() {
        getTabNavigator()
                .setActiveTabReopenedListener {
                    App.executor.execute(RemoveAll(sourceTag = tag!!))
                }
    }

    private fun initBackPressedListener() {
        addOnBackPressedListener {
            when {
                hasTabsInStack() -> App.executor.execute(RemoveLast(sourceTag = tag!!))
                else -> App.executor.execute(Finish())
            }
        }
    }

    private fun navigateToTab(type: MainTabType) {
        val route: FragmentRoute = when (type) {
            HOME -> HomeTabRoute()
            GALLERY -> GalleryRoute.Tab()
            PROFILE -> ProfileTabRoute()
        }
        App.executor.execute(
                Replace(
                        route = route,
                        animations = FadeAnimations,
                        sourceTag = tag!!
                )
        )
    }

    private fun hasTabsInStack(): Boolean {
        val backStackCount = getTabNavigator()
                .backStackEntryCount
        return backStackCount > 1
    }

    private fun getTabNavigator(): TabFragmentNavigatorInterface {
        return App.provider
                .provide()
                .fragmentNavigationProvider
                .provide(tag)
                .fragmentNavigator as TabFragmentNavigatorInterface
    }
}