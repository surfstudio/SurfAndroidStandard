package ru.surfstudio.android.navigation.navigator.fragment.tab.view

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigator

class ViewTabFragmentNavigator(
        override val fragmentManager: FragmentManager,
        override val containerId: Int,
        savedState: Bundle? = null
) : TabFragmentNavigator() {

    init {
        onRestoreState(savedState)
    }
}