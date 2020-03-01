package ru.surfstudio.android.navigation.navigator.fragment.view

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator

class ViewFragmentNavigator(
        override val fragmentManager: FragmentManager,
        override val containerId: Int,
        savedState: Bundle? = null
) : FragmentNavigator() {

    init {
        onRestoreState(savedState)
    }
}