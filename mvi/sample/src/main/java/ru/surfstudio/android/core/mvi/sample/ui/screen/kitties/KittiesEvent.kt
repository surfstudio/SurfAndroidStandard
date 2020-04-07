package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationComposition
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.Kitten
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class KittiesEvent : Event {

    object BackClicked : KittiesEvent()
    object KittenClicked : KittiesEvent()

    data class Navigation(override var events: List<NavigationEvent> = emptyList()) :
            KittiesEvent(), NavigationEvent, NavigationComposition

    data class Lifecycle(override var stage: LifecycleStage) :
            KittiesEvent(), LifecycleEvent

    sealed class TopKitten : KittiesEvent() {
        object UpdateClicked : TopKitten()
        object Load : TopKitten()

        data class Req(override var type: Request<Kitten> = Request.Loading()) :
                TopKitten(), RequestEvent<Kitten>
    }

    sealed class NewKittiesCount : KittiesEvent() {
        object UpdateClicked : NewKittiesCount()
        object Load : NewKittiesCount()

        data class Req(override var type: Request<Int> = Request.Loading()) :
                NewKittiesCount(), RequestEvent<Int>
    }

    sealed class PopularKitties : KittiesEvent() {
        object AllClicked : PopularKitties()
        object UpdateClicked : PopularKitties()
        object Load : PopularKitties()

        data class Req(override var type: Request<List<Kitten>> = Request.Loading()) :
                PopularKitties(), RequestEvent<List<Kitten>>
    }

    sealed class Meow : KittiesEvent() {
        object Clicked : Meow()
        object Send : Meow()
        object UpdateCount : Meow()

        data class SendReq(override var type: Request<Unit> = Request.Loading()) :
                Meow(), RequestEvent<Unit>

        data class UpdateCountReq(override var type: Request<Int> = Request.Loading()) :
                Meow(), RequestEvent<Int>
    }
}