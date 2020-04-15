package ru.surfstudio.android.navigation.di.supplier

import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.di.supplier.holder.FragmentNavigationHolder

interface FragmentNavigationSupplier {

    fun obtain(command: FragmentNavigationCommand): FragmentNavigationHolder
}