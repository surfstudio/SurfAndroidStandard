package ru.surfstudio.android.core.ui.event.base.resolver

import ru.surfstudio.android.core.ui.event.base.ScreenEvent
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate

/**
 * Интерфейс-маркер резольверов способных буфферизовать  события
 */
interface Storeable<D : ScreenEventDelegate, E: ScreenEvent> {

    val storedEvents: List<ScreenEvent>

    fun tryToResolve(delegate: D, event: E): Boolean
}