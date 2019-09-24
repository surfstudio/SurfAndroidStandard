package ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list

import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list.SimpleListEvent.*

/**
 * Хранитель состояния экрана [SimpleListActivityView]
 */
@PerScreen
class SimpleListStateHolder @Inject constructor() {
    val items = State(listOf<Int>())
}


/**
 * Реактор экрана [SimpleListActivityView]
 */
@PerScreen
class SimpleListReactor @Inject constructor() : Reactor<SimpleListEvent, SimpleListStateHolder> {
    override fun react(holder: SimpleListStateHolder, event: SimpleListEvent) {
        when (event) {
            is ListLoaded -> onListLoaded(holder, event)
            is StepperClicked -> onStepperClicked(holder, event)
        }
    }

    private fun onStepperClicked(holder: SimpleListStateHolder, event: StepperClicked) {
        holder.items.change {
            it.mapIndexed { index, i -> if (index == event.position) i + 1 else i }
        }
    }

    private fun onListLoaded(holder: SimpleListStateHolder, event: ListLoaded) {
        holder.items.accept(event.list)
    }
}