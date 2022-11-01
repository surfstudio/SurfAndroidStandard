package ru.surfstudio.standard.f_onboarding

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsComposition
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsEvent

internal sealed class OnboardingEvent : Event {

    data class Lifecycle(override var stage: LifecycleStage) : OnboardingEvent(), LifecycleEvent
    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) : OnboardingEvent(), NavCommandsComposition

    sealed class Input : OnboardingEvent() {
        object SkipBtnClicked : Input()
        object RemindLaterBtnClicked : Input()
    }
}
