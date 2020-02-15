package ru.surfstudio.android.navigation.navigator.callbacks

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator
import ru.surfstudio.android.navigation.navigator.fragment.container.FragmentContainer

class FragmentNavigatorHolder<T> where T : Fragment, T : FragmentContainer {

    val lifecycleCallbacks = Callbacks()

    var navigator: FragmentNavigator? = null

    fun register(fragment: T, savedInstanceState: Bundle?) {
        navigator = FragmentNavigator(
                fragment.childFragmentManager,
                fragment.containerId,
                savedInstanceState
        )

    }

    fun onSaveState(outState: Bundle?) {
        navigator?.onSaveState(outState)

    }

    fun unregister(fragment: T) {
        navigator = null
    }
}