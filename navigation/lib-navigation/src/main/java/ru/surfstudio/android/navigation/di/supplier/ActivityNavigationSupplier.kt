package ru.surfstudio.android.navigation.di.supplier

import ru.surfstudio.android.navigation.di.supplier.holder.ActivityNavigationHolder

interface ActivityNavigationSupplier {

    /**
     * Obtains [ActivityNavigationHolder] for current fully visible activity
     */
    fun obtain(): ActivityNavigationHolder

    /**
     * Determines, whether this supplier has current holder, or it is not yet initialized
     */
    fun hasCurrentHolder(): Boolean

    /**
     * Sets the listener, which will be invoked when [ActivityNavigationHolder] will become active.
     */
    fun setOnHolderActiveListenerSingle(listener: (ActivityNavigationHolder) -> Unit)
}