package ru.surfstudio.android.navigation.supplier

import ru.surfstudio.android.navigation.supplier.holder.FragmentNavigationHolder

interface FragmentNavigationSupplier {

    /**
     * Obtains [FragmentNavigationHolder] for current visible activity.
     *
     * @param sourceTag Tag of a source fragment, which will execute navigation command.
     *
     * @see [ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand.sourceTag]
     */
    fun obtain(sourceTag: String): FragmentNavigationHolder
}