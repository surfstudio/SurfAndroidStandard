package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationComposition
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.data.Kitten
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class KittiesEvent : Event {

    sealed class Input : KittiesEvent() {
        object BackClicked : Input()
        object TopKittenUpdateClicked : Input()
        object NewKittiesCountUpdateClicked : Input()
        object PopularKittiesUpdateClicked : Input()
        object PopularKittiesAllClicked : Input()
        object MeowClicked : Input()

        data class KittenClicked(val kitten: Kitten) : Input()
    }

    sealed class Data : KittiesEvent() {
        object LoadTopKitten : Data()
        object LoadNewKittiesCount : Data()
        object LoadPopularKittes : Data()
        object SendMeow : Data()
        object UpdateMeowCount : Data()
    }

    data class TopKittenRequestEvent(override var request: Request<Kitten>) :
            KittiesEvent(), RequestEvent<Kitten>

    data class NewKittiesCountRequestEvent(override var request: Request<Int>) :
            KittiesEvent(), RequestEvent<Int>

    data class PopularKittiesRequestEvent(override var request: Request<List<Kitten>>) :
            KittiesEvent(), RequestEvent<List<Kitten>>

    data class SendMeowRequestEvent(override var request: Request<Unit>) :
            KittiesEvent(), RequestEvent<Unit>

    data class UpdateMeowCountRequestEvent(override var request: Request<Int>) :
            KittiesEvent(), RequestEvent<Int>

    data class Navigation(override var events: List<NavigationEvent> = emptyList()) :
            KittiesEvent(), NavigationEvent, NavigationComposition

    data class Lifecycle(override var stage: LifecycleStage) :
            KittiesEvent(), LifecycleEvent
}