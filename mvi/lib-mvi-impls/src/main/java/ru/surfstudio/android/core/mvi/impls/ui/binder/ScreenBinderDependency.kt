package ru.surfstudio.android.core.mvi.impls.ui.binder

import ru.surfstudio.android.core.mvi.impls.ui.freezer.SubscriptionFreezer
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager

/**
 * [ScreenBinder] dependency
 */
data class ScreenBinderDependency(
        val eventDelegateManager: ScreenEventDelegateManager,
        val subscriptionFreezer: SubscriptionFreezer
)