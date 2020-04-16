package ru.surfstudio.android.navigation.supplier.callbacks

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import ru.surfstudio.android.navigation.supplier.id.IdentifiableScreen
import ru.surfstudio.android.navigation.supplier.ActivityNavigationSupplier
import ru.surfstudio.android.navigation.supplier.holder.ActivityNavigationHolder
import ru.surfstudio.android.navigation.supplier.FragmentNavigationSupplier
import ru.surfstudio.android.navigation.supplier.callbacks.creator.FragmentNavigationSupplierCallbacksCreator
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigator
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigator

//@PerApp
open class ActivityNavigationSupplierCallbacks(
        private val nestedCallbacksCreator: FragmentNavigationSupplierCallbacksCreator = ::FragmentNavigationSupplierCallbacks
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
        safeRequireActivityId(activity) { id ->
            val newHolder = createHolder(activity, savedInstanceState)
            navigatorHolders[id] = newHolder
            if (savedInstanceState != null) {
                currentHolder = newHolder
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
        //empty
    }

    override fun onActivityResumed(activity: Activity) {
        safeRequireActivityId(activity) { id ->
            val currentHolder = navigatorHolders[id]
            this.currentHolder = currentHolder
            onHolderActiveListenerSingle?.invoke(currentHolder ?: return@safeRequireActivityId)
            onHolderActiveListenerSingle = null
        }
    }

    override fun onActivityPaused(activity: Activity) {
        safeRequireActivityId(activity) { id ->
            val currentHolder = navigatorHolders[id]
            if (this.currentHolder == currentHolder) {
                this.currentHolder = null
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        safeRequireActivityId(activity) { id ->
            val nestedSupplier = navigatorHolders[id]?.fragmentSupplier
            val castedNestedSupplier = nestedSupplier as? FragmentNavigationSupplierCallbacks
            castedNestedSupplier?.onActivitySaveState(outState)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        //empty
    }

    override fun onActivityDestroyed(activity: Activity) {
        destroyHolder(activity)
    }

    private fun destroyHolder(activity: Activity) {
        safeRequireActivityId(activity) { id ->
            val nestedSupplier = navigatorHolders[id]?.fragmentSupplier
            unregisterFragmentNavigationSupplier(activity, nestedSupplier)
            navigatorHolders.remove(id)
        }
    }

    private fun registerFragmentNavigationSupplier(
            activity: Activity,
            nestedNavigationSupplier: FragmentNavigationSupplierCallbacks
    ) {
        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.registerFragmentLifecycleCallbacks(nestedNavigationSupplier, true)
    }

    private fun unregisterFragmentNavigationSupplier(
            activity: Activity,
            nestedNavigationSupplier: FragmentNavigationSupplier?
    ) {
        if (nestedNavigationSupplier !is FragmentNavigationSupplierCallbacks) return
        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.unregisterFragmentLifecycleCallbacks(nestedNavigationSupplier)
    }

    protected open fun createHolder(activity: Activity, savedInstanceState: Bundle?): ActivityNavigationHolder {
        require(activity is AppCompatActivity) { "All activities with ActivityNavigationHolders should implement AppCompatActivity!" }

        val nestedNavigationSupplier = nestedCallbacksCreator(activity, savedInstanceState)
        registerFragmentNavigationSupplier(activity, nestedNavigationSupplier)

        val activityNavigator = ActivityNavigator(activity)
        val dialogNavigator = DialogNavigator(activity)

        return ActivityNavigationHolder(
                activityNavigator,
                dialogNavigator,
                nestedNavigationSupplier
        )
    }

    protected open fun safeRequireActivityId(activity: Activity, onActivityReady: (id: String) -> Unit) {
        if (activity is AppCompatActivity && activity is IdentifiableScreen) {
            onActivityReady(activity.screenId)
        }
    }
}