package ru.surfstudio.android.navigation.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.activity.view.ViewActivityNavigator
import java.lang.IllegalStateException

//@PerApp
class ActivityNavigationSupplier(
        private val nestedCallbacks: FragmentManager.FragmentLifecycleCallbacks
) : Application.ActivityLifecycleCallbacks {

    private val navigators = hashMapOf<String, ActivityNavigatorHolder?>()

    private var currentNavigator: ActivityNavigatorHolder? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val newHolder = createHolder(activity as AppCompatActivity)
        navigators[getActivityId(activity)] = newHolder
        if (savedInstanceState != null) {
            currentNavigator = newHolder
        }
    }

    fun setupNestedNavigationHolder(activity: Activity) {
        if (activity !is FragmentActivity) return
        if (activity !is FragmentContainer) return

        val containerId = activity.containerId
        val fragmentManager = activity.supportFragmentManager

        fragmentManager.registerFragmentLifecycleCallbacks(nestedCallbacks, true)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        val currentHolder = navigators[getActivityId(activity)]
        currentNavigator = currentHolder
    }

    override fun onActivityPaused(activity: Activity) {
        val currentHolder = navigators[getActivityId(activity)]
        if (currentNavigator == currentHolder) {
            currentNavigator = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
//        navigators[getActivityId(activity)]?.onSaveState(outState)
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        destroyHolders(activity)
    }

    private fun destroyHolders(activity: Activity) {
        navigators[getActivityId(activity)] = null

        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager

        fragmentManager.unregisterFragmentLifecycleCallbacks(nestedCallbacks)
    }

    private fun getActivityId(activity: Activity): String = (activity as? IdentifiableScreen)?.screenId
            ?: throw IllegalStateException("Activity must implement from HasId.id method ")

    private fun createHolder(activity: AppCompatActivity): ActivityNavigatorHolder {
        val activityNavigator = ViewActivityNavigator(activity)
//        val dialogNavigator = ...
        return ActivityNavigatorHolder(activityNavigator)
    }
}