package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close

import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent

/**
 * Base close screen event.
 * Doesn't close any screen by itself,
 * to close specific screen, you need to it one of it's accessors.
 * @see [CloseActivityEvent]
 * @see [CloseTaskEvent]
 * @see [CloseWithResultEvent]
 * @see [CloseDialogEvent]
 * @see [CloseFragmentEvent]
 */
interface CloseScreenEvent : NavigationEvent