package ru.surfstudio.android.navigation.di.supplier.holder

import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface

open class FragmentNavigationHolder(
        val fragmentNavigator: FragmentNavigatorInterface,
        val tabFragmentNavigator: FragmentNavigatorInterface
)