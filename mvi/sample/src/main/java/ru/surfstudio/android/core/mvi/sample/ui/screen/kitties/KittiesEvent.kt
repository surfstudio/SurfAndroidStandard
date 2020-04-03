package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationComposition
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.Kitty
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class KittiesEvent : Event {

    data class Navigation(override var events: List<NavigationEvent> = emptyList()) :
            KittiesEvent(), NavigationEvent, NavigationComposition

    data class Lifecycle(override var stage: LifecycleStage) :
            KittiesEvent(), LifecycleEvent

    sealed class Input : KittiesEvent() {
        object BackClicked : Input()
        object LoadTopKittyNameClicked : Input()
        object LoadKittiesCountClicked : Input()
        object LoadKittiesListClicked : Input()
        object SendMeowClicked : Input()
    }

    sealed class Data : KittiesEvent() {
        object LoadTopKittyName : Data()
        object LoadKittiesCount : Data()
        object LoadKittiesList : Data()
        object SendMeow : Data()
    }

    // TODO Сделать ненадобным указывать в конструкторе Request.Loading()
    data class LoadTopKittyNameRequestEvent(override var type: Request<String> = Request.Loading()) :
            KittiesEvent(), RequestEvent<String>

    data class LoadKittiesCountRequestEvent(override var type: Request<Int> = Request.Loading()) :
            KittiesEvent(), RequestEvent<Int>

    data class LoadKittiesListRequestEvent(override var type: Request<List<Kitty>> = Request.Loading()) :
            KittiesEvent(), RequestEvent<List<Kitty>>

    data class SendMeowRequestEvent(override var type: Request<Unit> = Request.Loading()) :
            KittiesEvent(), RequestEvent<Unit>

}