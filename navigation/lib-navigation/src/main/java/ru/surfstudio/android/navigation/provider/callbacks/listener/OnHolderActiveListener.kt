package ru.surfstudio.android.navigation.provider.callbacks.listener

import ru.surfstudio.android.navigation.provider.holder.ActivityNavigationHolder

/**
 * Listener that invokes when any [ActivityNavigationHolder] becomes active (when any activity becomes resumed).
 */
typealias OnHolderActiveListener = (ActivityNavigationHolder) -> Unit