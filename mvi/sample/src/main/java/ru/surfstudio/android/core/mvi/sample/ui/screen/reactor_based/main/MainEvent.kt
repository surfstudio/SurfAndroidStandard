package ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.main

import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationComposition
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage

/**
 * Событие главного экрана
 */
sealed class MainEvent : Event {

    data class Lifecycle(override var stage: LifecycleStage) : MainEvent(), LifecycleEvent
    data class Navigation(override var events: List<NavigationEvent> = listOf()) : NavigationComposition, MainEvent()
}
