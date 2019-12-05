package ru.surfstudio.android.core.mvi.impls.ui.dialog

import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.event.Event

/**
 * Компонент простого диалога, в который необходимо заинжектить типизированный [ScreenEventHub]
 */
interface EventHubDialogComponent<E : Event> {

    fun screenHub(): ScreenEventHub<E>
}
