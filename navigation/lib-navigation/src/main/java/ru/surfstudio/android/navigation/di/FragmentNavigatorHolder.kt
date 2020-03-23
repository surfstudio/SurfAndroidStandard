package ru.surfstudio.android.navigation.di

import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface

class FragmentNavigatorHolder(
        val id: String,
        val fragmentNavigator: FragmentNavigatorInterface,
        val tabFragmentNavigator: FragmentNavigatorInterface
) {
}