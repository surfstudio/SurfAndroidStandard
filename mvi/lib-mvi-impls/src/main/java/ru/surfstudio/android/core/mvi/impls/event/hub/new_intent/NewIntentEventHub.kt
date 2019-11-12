package ru.surfstudio.android.core.mvi.impls.event.hub.new_intent

import android.content.Intent
import ru.surfstudio.android.core.mvi.event.factory.EventFactory
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.EventHub
import ru.surfstudio.android.core.ui.event.newintent.NewIntentDelegate

/**
 * [EventHub], обрабатывающий события OnNewIntent (поступление нового intent в Activity)
 */
interface NewIntentEventHub<E : Event, EventStream> : NewIntentDelegate, EventHub<E, EventStream> {

    val newIntentCreator: EventFactory<Intent, E>?

    override fun onNewIntent(intent: Intent): Boolean {
        val event = newIntentCreator?.invoke(intent) ?: return false
        emit(event)
        return true
    }
}