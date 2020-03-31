package ru.surfstudio.android.navigation.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import ru.surfstudio.android.navigation.navigator.activity.view.ViewActivityNavigator
import java.lang.IllegalStateException

//@PerApp
class ActivityNavigationSupplier(
        private val nestedCallbacksCreator: () -> FragmentNavigationSupplier
) : Application.ActivityLifecycleCallbacks {

    private val navigatorHolders = hashMapOf<String, ActivityNavigatorHolder?>()

    var currentHolder: ActivityNavigatorHolder? = null
        private set


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val newHolder = createHolder(activity as AppCompatActivity)
        navigatorHolders[getActivityId(activity)] = newHolder
        if (savedInstanceState != null) {
            currentHolder = newHolder
        }
    }

    override fun onActivityStarted(activity: Activity) {
        //empty
    }

    override fun onActivityResumed(activity: Activity) {
        val currentHolder = navigatorHolders[getActivityId(activity)]
        this.currentHolder = currentHolder
    }

    override fun onActivityPaused(activity: Activity) {
        val currentHolder = navigatorHolders[getActivityId(activity)]
        if (this.currentHolder == currentHolder) {
            this.currentHolder = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        //empty
    }

    override fun onActivityStopped(activity: Activity) {
        //empty
    }

    override fun onActivityDestroyed(activity: Activity) {
        destroyHolder(activity)
    }

    private fun destroyHolder(activity: Activity) {
        val id = getActivityId(activity)
        unregisterNestedNavigationSupplier(activity, navigatorHolders[id]?.nestedNavigationSupplier)
        navigatorHolders[id] = null
    }

    private fun unregisterNestedNavigationSupplier(
            activity: Activity,
            nestedNavigationSupplier: FragmentNavigationSupplier?
    ) {
        nestedNavigationSupplier ?: return
        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.unregisterFragmentLifecycleCallbacks(nestedNavigationSupplier)
    }

    private fun registerNestedNavigationSupplier(
            activity: Activity,
            nestedNavigationSupplier: FragmentNavigationSupplier
    ) {
        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.registerFragmentLifecycleCallbacks(nestedNavigationSupplier, true)
    }

    private fun getActivityId(activity: Activity): String =
            (activity as? IdentifiableScreen)?.screenId
                    ?: throw IllegalStateException("Activity must implement from HasId.id method ")

    private fun createHolder(activity: Activity): ActivityNavigatorHolder? {
        if (activity !is AppCompatActivity) return null

        val nestedNavigationSupplier = nestedCallbacksCreator()
        registerNestedNavigationSupplier(activity, nestedNavigationSupplier)

        val activityNavigator = ViewActivityNavigator(activity)
//        val dialogNavigator = ...
        return ActivityNavigatorHolder(activityNavigator, nestedNavigationSupplier)
    }
}