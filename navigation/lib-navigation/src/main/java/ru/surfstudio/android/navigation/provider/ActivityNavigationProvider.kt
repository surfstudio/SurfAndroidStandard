package ru.surfstudio.android.navigation.provider

import ru.surfstudio.android.navigation.provider.holder.ActivityNavigationHolder

interface ActivityNavigationProvider {

    /**
     * Obtains [ActivityNavigationHolder] for current fully visible activity
     */
    fun provide(): ActivityNavigationHolder

    /**
     * Determines, whether this provider has current holder, or it is not yet initialized
     */
    fun hasCurrentHolder(): Boolean

    /**
     * Sets the listener, which will be invoked when [ActivityNavigationHolder] will become active.
     */
    fun setOnHolderActiveListenerSingle(listener: (ActivityNavigationHolder) -> Unit)
}