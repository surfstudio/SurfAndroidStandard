package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list.SimpleListEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list.controller.StepperData
import ru.surfstudio.android.core.mvi.ui.reducer.Reducer

/**
 * Хранитель состояния экрана [SimpleListActivityView]
 */
data class SimpleListModel(
        val items: List<StepperData> = listOf()
)

/**
 * Реактор экрана [SimpleListActivityView]
 */
@PerScreen
class SimpleListReducer @Inject constructor(
        baseReactorDependency: BaseReactorDependency
) : BaseReducer<SimpleListEvent, SimpleListModel>(baseReactorDependency) {

    override fun reduce(state: SimpleListModel, event: SimpleListEvent): SimpleListModel {
        return when (event) {
            is ListLoaded -> onListLoaded(state, event)
            is StepperClicked -> onStepperClicked(state, event)
            else -> state
        }
    }

    private fun onStepperClicked(state: SimpleListModel, event: StepperClicked): SimpleListModel =
            state.copy(items = state.items.map {
                if (it.id == event.id) it.copy(clicks = it.clicks + 1) else it
            })

    private fun onListLoaded(state: SimpleListModel, event: ListLoaded): SimpleListModel =
            state.copy(items = event.list)
}