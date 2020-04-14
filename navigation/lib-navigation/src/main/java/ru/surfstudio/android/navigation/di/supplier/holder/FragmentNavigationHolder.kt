package ru.surfstudio.android.navigation.di.supplier.holder

import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface

class FragmentNavigationHolder(
        val id: String,
        val fragmentNavigator: FragmentNavigatorInterface,
        val tabFragmentNavigator: FragmentNavigatorInterface
)