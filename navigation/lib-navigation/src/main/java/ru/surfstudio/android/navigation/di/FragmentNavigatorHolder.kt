package ru.surfstudio.android.navigation.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface

class FragmentNavigatorHolder(
        val navigatorCreator: FragmentNavigatorCreator
) : Application.ActivityLifecycleCallbacks, NavigatorHolder<FragmentNavigatorInterface> {

    private val navigators = hashMapOf<String, FragmentNavigatorInterface?>()

    private var currentNavigator: FragmentNavigatorInterface? = null

    override val navigator: FragmentNavigatorInterface
        get() = currentNavigator!!

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val fragmentManager = (activity as? FragmentActivity)?.supportFragmentManager ?: return
        val containerId = (activity as? FragmentContainer)?.containerId ?: return
        val newNavigator = navigatorCreator(fragmentManager, containerId, savedInstanceState)
        navigators[getActivityId(activity)] = newNavigator
        if (savedInstanceState != null) {
            currentNavigator = newNavigator
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        val currentActivityNavigator = navigators[getActivityId(activity)]
        currentNavigator = currentActivityNavigator
    }

    override fun onActivityPaused(activity: Activity) {
        val currentActivityNavigator = navigators[getActivityId(activity)]
        if (currentNavigator == currentActivityNavigator) {
            currentNavigator = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        navigators[getActivityId(activity)]?.onSaveState(outState)
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        navigators[getActivityId(activity)] = null
    }

    private fun getActivityId(activity: Activity): String = "${activity.hashCode()}"
}