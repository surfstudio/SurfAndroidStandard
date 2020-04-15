package ru.surfstudio.android.navigation.di.supplier.callbacks

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import ru.surfstudio.android.navigation.di.IdentifiableScreen
import ru.surfstudio.android.navigation.di.supplier.ActivityNavigationSupplier
import ru.surfstudio.android.navigation.di.supplier.holder.ActivityNavigationHolder
import ru.surfstudio.android.navigation.di.supplier.FragmentNavigationSupplier
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigator
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigator
import java.lang.IllegalStateException

//@PerApp
class ActivityNavigationSupplierCallbacks(
        private val nestedCallbacksCreator: (AppCompatActivity, Bundle?) -> FragmentNavigationSupplierCallbacks
) : Application.ActivityLifecycleCallbacks, ActivityNavigationSupplier {


    private val navigatorHolders = hashMapOf<String, ActivityNavigationHolder?>()

    var currentHolder: ActivityNavigationHolder? = null
        private set

    override fun obtain(): ActivityNavigationHolder {
        return currentHolder ?: error("Navigation holder is empty. You have no visible activity.")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val newHolder = createHolder(activity, savedInstanceState)
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
        val id = getActivityId(activity)
        navigatorHolders
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
        if (nestedNavigationSupplier !is FragmentNavigationSupplierCallbacks) return
        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.unregisterFragmentLifecycleCallbacks(nestedNavigationSupplier)
    }

    private fun registerNestedNavigationSupplier(
            activity: Activity,
            nestedNavigationSupplier: FragmentNavigationSupplierCallbacks
    ) {
        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.registerFragmentLifecycleCallbacks(nestedNavigationSupplier, true)
    }

    private fun getActivityId(activity: Activity): String {
        val identifiableActivity = activity as? IdentifiableScreen
        val activityId = identifiableActivity?.screenId
        requireNotNull(activityId) { "Activity must implement IdentifiableScreen.screenId method" }
        return activityId
    }

    private fun createHolder(activity: Activity, savedInstanceState: Bundle?): ActivityNavigationHolder? {
        if (activity !is AppCompatActivity) return null

        val nestedNavigationSupplier = nestedCallbacksCreator(activity, savedInstanceState)
        registerNestedNavigationSupplier(activity, nestedNavigationSupplier)

        val activityNavigator = ActivityNavigator(activity)
        val dialogNavigator = DialogNavigator(activity)
        return ActivityNavigationHolder(activityNavigator, dialogNavigator, nestedNavigationSupplier)
    }
}