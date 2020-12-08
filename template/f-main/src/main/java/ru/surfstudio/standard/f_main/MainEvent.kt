package ru.surfstudio.standard.f_main

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsComposition
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsEvent

//todo Класс публичный для работы инструментальных тестов
sealed class MainEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) : NavCommandsComposition, MainEvent()
    data class Lifecycle(override var stage: LifecycleStage) : MainEvent(), LifecycleEvent
}
