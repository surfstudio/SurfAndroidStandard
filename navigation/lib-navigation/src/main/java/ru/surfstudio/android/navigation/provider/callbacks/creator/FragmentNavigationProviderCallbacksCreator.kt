package ru.surfstudio.android.navigation.provider.callbacks.creator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.provider.callbacks.FragmentNavigationProviderCallbacks

/**
 * Lambda that creates [FragmentNavigationProviderCallbacks] with activity and its saved instance state.
 */
typealias FragmentNavigationProviderCallbacksCreator = (
        parentActivity: AppCompatActivity,
        savedInstance: Bundle?
) -> FragmentNavigationProviderCallbacks