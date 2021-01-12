package ru.surfstudio.standard.ui.dialog.simple

import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.event.Event

/**
 * Компонент простого диалога, в который необходимо заинжектить типизированный [ScreenEventHub]
 */
interface SimpleEventHubDialogComponent<E : Event> {

    fun screenHub(): ScreenEventHub<E>
}