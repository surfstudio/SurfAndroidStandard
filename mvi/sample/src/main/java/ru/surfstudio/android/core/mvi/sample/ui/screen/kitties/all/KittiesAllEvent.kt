package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.all

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationComposition
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.Kitten
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList

internal sealed class KittiesAllEvent : Event {

    sealed class Input : KittiesAllEvent() {
        object BackClicked : Input()
        object SwrReload : Input()
        object LoadNext : Input()
    }

    sealed class Data : KittiesAllEvent() {
        data class LoadKitties(val offset: Int, val isSwr: Boolean) : Data()
    }

    data class LoadKittiesRequestEvent(
            override val request: Request<DataList<Kitten>>,
            val isSwr: Boolean
    ) : KittiesAllEvent(), RequestEvent<DataList<Kitten>>

    data class Navigation(override var events: List<NavigationEvent> = emptyList()) :
            KittiesAllEvent(), NavigationComposition

    data class Lifecycle(override var stage: LifecycleStage) :
            KittiesAllEvent(), LifecycleEvent
}