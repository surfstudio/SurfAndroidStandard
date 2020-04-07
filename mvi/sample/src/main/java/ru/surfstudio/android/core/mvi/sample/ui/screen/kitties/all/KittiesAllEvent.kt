package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.all

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationComposition
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.Kitten
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList


internal sealed class KittiesAllEvent : Event {

    object BackClicked : KittiesAllEvent()

    sealed class Content : KittiesAllEvent() {
        object LoadSwr : Content()
        object LoadNext : Content()

        data class Load(val offset: Int, val isSwr: Boolean) : Content()

        data class Req(
                override var type: Request<DataList<Kitten>> = Request.Loading(),
                val isSwr: Boolean = false
        ) : Content(), RequestEvent<DataList<Kitten>>
    }

    data class Navigation(override var events: List<NavigationEvent> = emptyList()) :
            KittiesAllEvent(), NavigationComposition

    data class Lifecycle(override var stage: LifecycleStage) :
            KittiesAllEvent(), LifecycleEvent
}