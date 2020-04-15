package ru.surfstudio.android.navigation.di.supplier.callbacks

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import ru.surfstudio.android.navigation.di.IdentifiableScreen
import ru.surfstudio.android.navigation.di.supplier.holder.ActivityNavigationHolder
import ru.surfstudio.android.navigation.di.supplier.FragmentNavigationSupplier
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigator
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigator
import java.lang.IllegalStateException

//@PerApp
class ActivityNavigationSupplierCallbacks(
        private val nestedCallbacksCreator: () -> FragmentNavigationSupplierCallbacks
) : Application.ActivityLifecycleCallbacks {

    private val navigatorHolders = hashMapOf<String, ActivityNavigationHolder?>()

    var currentHolder: ActivityNavigationHolder? = null
        private set


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val newHolder = createHolder(activity)
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

    private fun getActivityId(activity: Activity): String =
            (activity as? IdentifiableScreen)?.screenId
                    ?: throw IllegalStateException("Activity must implement from HasId.id method ")

    private fun createHolder(activity: Activity): ActivityNavigationHolder? {
        if (activity !is AppCompatActivity) return null

        val nestedNavigationSupplier = nestedCallbacksCreator()
        registerNestedNavigationSupplier(activity, nestedNavigationSupplier)

        val activityNavigator = ActivityNavigator(activity)
        val dialogNavigator = DialogNavigator(activity)
        return ActivityNavigationHolder(activityNavigator, dialogNavigator, nestedNavigationSupplier)
    }
}