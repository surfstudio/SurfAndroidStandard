package ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseMapMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list.SimpleListEvent.*
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.network.toObservable

/**
 * Middleware экрана [SimpleListActivityView]
 */
@PerScreen
class SimpleListMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseMapMiddleware<SimpleListEvent>(baseMiddlewareDependency) {

    override fun flatMap(event: SimpleListEvent): Observable<out SimpleListEvent> = when (event) {
        is SimpleListLifecycle -> reactOnLifecycle(event)
        else -> skip()
    }

    fun reactOnLifecycle(event: SimpleListLifecycle): Observable<out SimpleListEvent> =
            when (event.stage) {
                LifecycleStage.CREATED ->
                    ListLoaded(listOf(1, 2, 3)).toObservable()
                else -> skip()
            }
}