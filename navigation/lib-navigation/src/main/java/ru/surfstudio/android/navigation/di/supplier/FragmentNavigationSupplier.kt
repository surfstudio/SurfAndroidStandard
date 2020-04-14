package ru.surfstudio.android.navigation.di.supplier

import ru.surfstudio.android.navigation.di.supplier.holder.FragmentNavigationHolder

interface FragmentNavigationSupplier {

    var currentLevel: Int
    val currentHolder: FragmentNavigationHolder?
}