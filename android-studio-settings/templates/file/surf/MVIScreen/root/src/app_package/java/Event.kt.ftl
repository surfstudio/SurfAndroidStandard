package ${packageName}

import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationComposition
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class ${eventClassName}: Event {

    data class Lifecycle(override var stage: LifecycleStage): ${eventClassName}(), LifecycleEvent
    data class Navigation(
        override var events: List<NavigationEvent> = listOf()
    ) : ${eventClassName}(), NavigationComposition
}
