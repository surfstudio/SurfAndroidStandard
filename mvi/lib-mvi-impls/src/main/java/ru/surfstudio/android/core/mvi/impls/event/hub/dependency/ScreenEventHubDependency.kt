package ru.surfstudio.android.core.mvi.impls.event.hub.dependency

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState

/**
 * Dependency for [ScreenEventHub]
 */
class ScreenEventHubDependency(
        val screenState: ScreenState,
        val screenEventDelegate: ScreenEventDelegateManager
)