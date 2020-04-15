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

//@PerApp
open class ActivityNavigationSupplierCallbacks(
        private val nestedCallbacksCreator: (AppCompatActivity, Bundle?) -> FragmentNavigationSupplierCallbacks
) : Application.ActivityLifecycleCallbacks, ActivityNavigationSupplier {


    private val navigatorHolders = hashMapOf<String, ActivityNavigationHolder>()
    private var currentHolder: ActivityNavigationHolder? = null
    private var onHolderActiveListenerSingle: ((ActivityNavigationHolder) -> Unit)? = null

    override fun obtain(): ActivityNavigationHolder {
        return currentHolder ?: error("Navigation holder is empty. You have no visible activity.")
    }

    override fun hasCurrentHolder(): Boolean {
        return currentHolder != null
    }

    override fun setOnHolderActiveListenerSingle(listener: (ActivityNavigationHolder) -> Unit) {
        onHolderActiveListenerSingle = listener
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val newHolder = createHolder(activity, savedInstanceState)
        navigatorHolders[getActivityId(activity)] = newHolder
        if (savedInstanceState == null) {
            currentHolder = newHolder
        }
    }

    override fun onActivityStarted(activity: Activity) {
        //empty
    }

    override fun onActivityResumed(activity: Activity) {
        val currentHolder = navigatorHolders[getActivityId(activity)]
        this.currentHolder = currentHolder
        onHolderActiveListenerSingle?.invoke(currentHolder ?: return)
        onHolderActiveListenerSingle = null
    }

    override fun onActivityPaused(activity: Activity) {
        val currentHolder = navigatorHolders[getActivityId(activity)]
        if (this.currentHolder == currentHolder) {
            this.currentHolder = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        val id = getActivityId(activity)
        val nestedCallbacks = navigatorHolders[id]?.nestedNavigationSupplier as? FragmentNavigationSupplierCallbacks
        nestedCallbacks?.onActivitySaveState(outState)
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
        navigatorHolders.remove(id)
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

    protected open fun createHolder(activity: Activity, savedInstanceState: Bundle?): ActivityNavigationHolder {
        require(activity is AppCompatActivity) { "All activities should implement AppCompatActivity!" }

        val nestedNavigationSupplier = nestedCallbacksCreator(activity, savedInstanceState)
        registerNestedNavigationSupplier(activity, nestedNavigationSupplier)

        val activityNavigator = ActivityNavigator(activity)
        val dialogNavigator = DialogNavigator(activity)
        return ActivityNavigationHolder(
                activity,
                activityNavigator,
                dialogNavigator,
                nestedNavigationSupplier
        )
    }
}