package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMapMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list.SimpleListEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list.controller.StepperData
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.rx.extension.toObservable

/**
 * Middleware экрана [SimpleListActivityView]
 */
@PerScreen
class SimpleListMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseMapMiddleware<SimpleListEvent>(baseMiddlewareDependency) {

    override fun flatMap(event: SimpleListEvent): Observable<out SimpleListEvent> = when (event) {
        is Lifecycle -> reactOnLifecycle(event)
        else -> skip()
    }

    fun reactOnLifecycle(event: Lifecycle): Observable<out SimpleListEvent> =
            when (event.stage) {
                LifecycleStage.CREATED ->
                    ListLoaded(
                            listOf(
                                    StepperData(0),
                                    StepperData(1),
                                    StepperData(2)
                            )
                    ).toObservable()
                else -> skip()
            }
}