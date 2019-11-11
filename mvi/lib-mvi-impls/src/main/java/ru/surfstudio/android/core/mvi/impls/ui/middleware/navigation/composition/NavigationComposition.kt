package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.NavigationEvent
import java.io.Serializable

/**
 * Композиция для события навигации
 * Наследуется от [Serializable] чтобы обеспечить бесшовную передачу через Route.
 */
interface NavigationComposition : CompositionEvent<NavigationEvent>, Serializable