package ru.surfstudio.android.mvp.binding.rx.sample.react

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleStage
import ru.surfstudio.android.core.mvp.binding.react.rx_builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.react.rx_builders.RxBuilderIO
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.Middleware
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ListEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.reducer.ListStateHolder
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class ReactiveMiddleware @Inject constructor(
        errorHandler: ErrorHandler,
        schedulersProvider: SchedulersProvider,
        private val sh: ListStateHolder
) : BaseMiddleware<ListEvent>(schedulersProvider, errorHandler) {

    override fun flatMap(event: ListEvent): Observable<out ListEvent> {
        return when (event) {
            is ListEvent.Lifecycle -> reactOnLifecycle(event.stage)
            is ListEvent.Reload -> loadData()
            is ListEvent.LoadNextPage -> loadData(sh.state.data.nextPage)
            else -> skipEvent()
        }
    }

    private fun reactOnLifecycle(stage: LifecycleStage): Observable<out ListEvent> =
            when (stage) {
                LifecycleStage.CREATE -> loadData()
                else -> skipEvent()
            }

    private fun loadData(page: Int = 0) = createObservable(page)
            .io()
            .handleError()
            .mapToLoadable(ListEvent.LoadList())

    private fun createObservable(page: Int = 0) = Observable.timer(2, TimeUnit.SECONDS).map {
        if (page == 0)
            DataList<String>(
                    (1..20).map { (it + page * 20).toString() },
                    page,
                    20
            )
        else throw IOException()
    }
}