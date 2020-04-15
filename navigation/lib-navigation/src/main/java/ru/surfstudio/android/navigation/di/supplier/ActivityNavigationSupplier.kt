package ru.surfstudio.android.navigation.di.supplier

import ru.surfstudio.android.navigation.di.supplier.holder.ActivityNavigationHolder

interface ActivityNavigationSupplier {

    fun obtain(): ActivityNavigationHolder
}