package ru.surfstudio.standard.f_main.bar

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsComposition
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsEvent
import ru.surfstudio.standard.ui.navigation.routes.MainTabType

internal sealed class MainBarEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) : NavCommandsComposition, MainBarEvent()
    data class Lifecycle(override var stage: LifecycleStage) : MainBarEvent(), LifecycleEvent

    data class TabSelected(val tabType: MainTabType) : MainBarEvent()
}
