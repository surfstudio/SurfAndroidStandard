package ru.surfstudio.android.navigation.provider.holder

import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface

open class FragmentNavigationHolder(
        val fragmentNavigator: FragmentNavigatorInterface,
        val tabFragmentNavigator: TabFragmentNavigatorInterface
)