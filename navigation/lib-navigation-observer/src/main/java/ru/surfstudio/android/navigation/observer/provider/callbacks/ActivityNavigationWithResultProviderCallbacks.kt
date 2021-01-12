package ru.surfstudio.android.navigation.observer.provider.callbacks

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.observer.navigator.activity.ActivityNavigatorWithResult
import ru.surfstudio.android.navigation.observer.provider.ActivityNavigationWithResultHolder
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.provider.callbacks.factory.FragmentNavigationProviderCallbacksFactory
import ru.surfstudio.android.navigation.provider.holder.ActivityNavigationHolder

class ActivityNavigationWithResultProviderCallbacks(
        fragmentCallbacksFactory: FragmentNavigationProviderCallbacksFactory = FragmentNavigationProviderCallbacksFactory()
) : ActivityNavigationProviderCallbacks(fragmentCallbacksFactory) {

    override fun createHolder(id: String, activity: Activity, savedInstanceState: Bundle?): ActivityNavigationHolder {
        val holder = super.createHolder(id, activity, savedInstanceState)
        return ActivityNavigationWithResultHolder(
                ActivityNavigatorWithResult(activity as AppCompatActivity),
                holder.dialogNavigator,
                holder.fragmentNavigationProvider
        )
    }
}
