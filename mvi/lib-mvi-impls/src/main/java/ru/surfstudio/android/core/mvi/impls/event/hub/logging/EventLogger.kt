package ru.surfstudio.android.core.mvi.impls.event.hub.logging

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.logger.Logger

/**
 * Simple logger, that logs events consumed by EventHub
 */
open class EventLogger {

    open val shouldLog: Boolean = true

    /**
     * Log an event
     */
    open fun log(event: Event?, screenName: String) {
        if (shouldLog)
            Logger.d("Event / $screenName / $event")
    }
}