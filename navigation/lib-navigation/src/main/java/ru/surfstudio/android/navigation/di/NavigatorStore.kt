package ru.surfstudio.android.navigation.di

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface
import ru.surfstudio.android.navigation.navigator.activity.view.ViewActivityNavigator
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.route.TabRootRoute
import ru.surfstudio.android.navigation.navigator.fragment.tab.view.ViewTabFragmentNavigator
import ru.surfstudio.android.navigation.navigator.fragment.view.ViewFragmentNavigator
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

interface NavigatorStore {

    fun <R> obtainNavigator(route: R): FragmentNavigatorInterface?
            where R : FragmentContainer, R : FragmentRoute
}

interface ActivityProvider {

    fun get(): AppCompatActivity?
}


interface FragmentProvider {

    fun get(): Fragment?
}

class ActivityNavigatorStore(private val activityProvider: ActivityProvider) {

    fun <R> obtainNavigator(route: R): ActivityNavigatorInterface {
        //TODO отрефакторить на более адекватное поведение
        return ViewActivityNavigator(activityProvider.get()!!)
    }
}

class FragmentNavigatorStore(
        private val activityProvider: ActivityProvider,
        private val fragmentProvider: FragmentProvider
) : NavigatorStore {

    private val navigators = hashMapOf<Int, FragmentNavigatorInterface>()

    override fun <R> obtainNavigator(route: R): FragmentNavigatorInterface?
            where R : FragmentRoute, R : FragmentContainer {
        val containerId = route.containerId

        val existingNavigator = navigators[containerId]
        if (existingNavigator != null) return existingNavigator

        val currentFragment = fragmentProvider.get()
        val currentActivity = activityProvider.get()

        val newNavigator = when {
            currentFragment?.view?.findViewById<View>(containerId) != null ->
                createFragmentNavigator(currentFragment.childFragmentManager, route)
            currentActivity?.findViewById<View>(containerId) != null ->
                createFragmentNavigator(currentActivity.supportFragmentManager, route)
            else -> null
        }
        newNavigator?.let { navigators.put(containerId, it) }
        return newNavigator
    }

    private fun <R> createFragmentNavigator(
            fragmentManager: FragmentManager,
            route: R
    ): FragmentNavigatorInterface
            where R : FragmentRoute, R : FragmentContainer {
        return if (route is TabRootRoute) {
            ViewTabFragmentNavigator(fragmentManager, route.containerId)
        } else {
            ViewFragmentNavigator(fragmentManager, route.containerId)
        }
    }
}