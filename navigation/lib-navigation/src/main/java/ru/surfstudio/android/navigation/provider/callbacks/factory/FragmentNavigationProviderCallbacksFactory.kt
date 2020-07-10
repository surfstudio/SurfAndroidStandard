package ru.surfstudio.android.navigation.provider.callbacks.factory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.provider.callbacks.FragmentNavigationProviderCallbacks

/**
 * Factory that creates [FragmentNavigationProviderCallbacks] from parent activity and its saved instance state.
 */
open class FragmentNavigationProviderCallbacksFactory {

    open fun create(
            parentActivity: AppCompatActivity,
            savedInstance: Bundle?
    ): FragmentNavigationProviderCallbacks {
        return FragmentNavigationProviderCallbacks(parentActivity, savedInstance)
    }
}