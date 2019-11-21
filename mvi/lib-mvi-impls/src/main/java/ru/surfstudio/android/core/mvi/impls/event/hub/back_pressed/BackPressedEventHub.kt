package ru.surfstudio.android.core.mvi.impls.event.hub.back_pressed

import ru.surfstudio.android.core.mvi.event.factory.ParamlessEventFactory
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.EventHub
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate

/**
 * [EventHub], that handles BackPressedEvents (clicks on system "Back" button)
 */
interface BackPressedEventHub<E : Event, EventStream> : EventHub<E, EventStream>, OnBackPressedDelegate {

    val backPressedCreator: ParamlessEventFactory<E>?

    override fun onBackPressed(): Boolean {
        val event = backPressedCreator?.invoke() ?: return false
        emit(event)
        return true
    }
}