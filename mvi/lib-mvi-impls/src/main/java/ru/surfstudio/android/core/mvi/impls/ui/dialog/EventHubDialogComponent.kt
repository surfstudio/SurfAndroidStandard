package ru.surfstudio.android.core.mvi.impls.ui.dialog

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub

/**
 * Component for simple dialogs, which live in parent dagger scope.
 *
 * You need to inherit your parent's ScreenComponent from this component to
 * make injection of [ScreenEventHub] to a dialog possible.
 */
interface EventHubDialogComponent<E : Event> {

    fun screenHub(): ScreenEventHub<E>
}
