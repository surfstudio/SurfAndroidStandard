package ru.surfstudio.android.navigation.provider.callbacks

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.holder.ActivityNavigationHolder
import ru.surfstudio.android.navigation.provider.FragmentNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.creator.FragmentNavigationProviderCallbacksCreator
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigator
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigator
import ru.surfstudio.android.navigation.route.activity.ActivityRoute.Companion.SCREEN_ID

/**
 * Activity
 */
//@PerApp
open class ActivityNavigationProviderCallbacks(
        private val packageName: String,
        private val fragmentCallbacksCreator: FragmentNavigationProviderCallbacksCreator = ::FragmentNavigationProviderCallbacks
) : Application.ActivityLifecycleCallbacks, ActivityNavigationProvider {


    private val navigatorHolders = hashMapOf<String, ActivityNavigationHolder>()
    private var currentHolder: ActivityNavigationHolder? = null
    private var onHolderActiveListenerSingle: ((ActivityNavigationHolder) -> Unit)? = null

    override fun provide(): ActivityNavigationHolder {
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
            val provider = navigatorHolders[id]?.fragmentNavigationProvider
            val callbacksProvider = provider as? FragmentNavigationProviderCallbacks
            callbacksProvider?.onActivitySaveState(outState)
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
            val fragmentNavigationProvider = navigatorHolders[id]?.fragmentNavigationProvider
            unregisterFragmentNavigationProvider(activity, fragmentNavigationProvider)
            navigatorHolders.remove(id)
        }
    }

    private fun registerFragmentNavigationProvider(
            activity: Activity,
            fragmentNavigationProvider: FragmentNavigationProvider
    ) {
        if (fragmentNavigationProvider !is FragmentNavigationProviderCallbacks) return
        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.registerFragmentLifecycleCallbacks(fragmentNavigationProvider, true)
    }

    private fun unregisterFragmentNavigationProvider(
            activity: Activity,
            fragmentNavigationProvider: FragmentNavigationProvider?
    ) {
        if (fragmentNavigationProvider !is FragmentNavigationProviderCallbacks) return
        if (activity !is FragmentActivity) return
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.unregisterFragmentLifecycleCallbacks(fragmentNavigationProvider)
    }

    protected open fun createHolder(activity: Activity, savedInstanceState: Bundle?): ActivityNavigationHolder {
        require(activity is AppCompatActivity) { "All activities with ActivityNavigationHolders should implement AppCompatActivity!" }

        val fragmentNavigationProvider = fragmentCallbacksCreator(activity, savedInstanceState)
        registerFragmentNavigationProvider(activity, fragmentNavigationProvider)

        val activityNavigator = ActivityNavigator(activity)
        val dialogNavigator = DialogNavigator(activity)

        return ActivityNavigationHolder(
                activityNavigator,
                dialogNavigator,
                fragmentNavigationProvider
        )
    }

    protected open fun safeRequireActivityId(activity: Activity, onActivityReady: (id: String) -> Unit) {
        if (activity is AppCompatActivity) {
            val intent = activity.intent
            val screenId = when {
                intent.hasExtra(SCREEN_ID) -> intent.getStringExtra(SCREEN_ID)
                activity.packageName == packageName && intent.action == Intent.ACTION_MAIN -> LAUNCHER_ACTIVITY_ID
                else -> return
            }
            onActivityReady(screenId)
        }
    }

    private companion object {
        const val LAUNCHER_ACTIVITY_ID = "ActivityNavigationProviderCallbacks Launcher activity"
    }
}