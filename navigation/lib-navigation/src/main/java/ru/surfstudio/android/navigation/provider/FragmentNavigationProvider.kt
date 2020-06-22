package ru.surfstudio.android.navigation.provider

import ru.surfstudio.android.navigation.provider.holder.FragmentNavigationHolder

/**
 * Provider that can obtain [FragmentNavigationHolder].
 */
interface FragmentNavigationProvider {

    /**
     * Obtains [FragmentNavigationHolder] for current visible activity.
     *
     * @param sourceTag Tag of a source fragment, which will execute navigation command.
     *
     * @see [ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand.sourceTag]
     */
    fun provide(sourceTag: String?): FragmentNavigationHolder
}