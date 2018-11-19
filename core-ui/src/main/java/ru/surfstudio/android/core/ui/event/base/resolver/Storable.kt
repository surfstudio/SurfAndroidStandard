package ru.surfstudio.android.core.ui.event.base.resolver

import ru.surfstudio.android.core.ui.event.base.ScreenEvent

/**
 * Интерфейс-маркер резольверов способных буфферизовать  события
 */
interface Storable {

    val storedEvents: List<ScreenEvent>

}