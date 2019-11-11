package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation

import ru.surfstudio.android.core.mvi.event.Event
import java.io.Serializable

/**
 * Базовое событие навигации.
 *
 * Наследуется от [Serializable] чтобы обеспечить бесшовную передачу через Route.
 */
interface NavigationEvent : Event, Serializable