package ru.surfstudio.android.core.ui.event.base.resolver

import ru.surfstudio.android.core.ui.event.base.ScreenEvent

/**
 * Интерфейс-маркер резольверов способных буфферизовать необработанные события
 */
interface UnhandledEventsStore {

    val unhandledEvents: List<ScreenEvent>

}