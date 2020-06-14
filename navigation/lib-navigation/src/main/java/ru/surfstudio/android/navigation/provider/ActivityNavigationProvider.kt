package ru.surfstudio.android.navigation.provider

import ru.surfstudio.android.navigation.provider.callbacks.listener.OnHolderActiveListener
import ru.surfstudio.android.navigation.provider.holder.ActivityNavigationHolder
import ru.surfstudio.android.navigation.provider.holder.FragmentNavigationHolder

interface ActivityNavigationProvider {

    /**
     * Obtains [ActivityNavigationHolder] for current fully visible activity
     */
    fun provide(): ActivityNavigationHolder

    /**
     * Obtains [ActivityNavigationHolder] for activity with specific id.
     *
     * * Note: you shouldn't try to obtain nested [FragmentNavigationHolder]
     * and make fragment animation on inactive (paused) activity.
     * Fragment navigation is supported only for current visible activity, so better use [provide].
     *
     * @param id id of activity that we're trying to get.
     */
    fun provide(id: String): ActivityNavigationHolder?

    /**
     * Determines, whether this provider has current holder, or it is not yet initialized
     */
    fun hasCurrentHolder(): Boolean

    /**
     * Sets the listener, which will be invoked when [ActivityNavigationHolder] will become active.
     */
    fun setOnHolderActiveListenerSingle(listener: OnHolderActiveListener)
}