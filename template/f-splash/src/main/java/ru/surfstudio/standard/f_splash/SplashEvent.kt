package ru.surfstudio.standard.f_splash

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsComposition
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsEvent

/**
 * События сплэш экрана
 */
sealed class SplashEvent : Event {

    data class Lifecycle(override var stage: LifecycleStage) : SplashEvent(), LifecycleEvent
    data class Navigation(
            override var event: NavCommandsEvent = NavCommandsEvent()
    ) : SplashEvent(), NavCommandsComposition
}