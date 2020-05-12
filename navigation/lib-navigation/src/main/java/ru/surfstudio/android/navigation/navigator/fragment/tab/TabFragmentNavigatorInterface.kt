package ru.surfstudio.android.navigation.navigator.fragment.tab

import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.ActiveTabReopenedListener

interface TabFragmentNavigatorInterface : FragmentNavigatorInterface {

    val tabCount: Int

    fun setActiveTabReopenedListener(listener: ActiveTabReopenedListener?)
}