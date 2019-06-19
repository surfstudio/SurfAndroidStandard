package ru.surfstudio.android.mvp.binding.rx.sample.react

import android.util.Log
import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleStage
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ReactiveList
import ru.surfstudio.android.mvp.binding.rx.sample.react.reactor.ReactiveListStateHolder
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class ReactiveListMiddleware @Inject constructor(
        errorHandler: ErrorHandler,
        schedulersProvider: SchedulersProvider,
        private val sh: ReactiveListStateHolder
) : BaseMiddleware<ReactiveList>(schedulersProvider, errorHandler) {

    override fun transform(eventStream: Observable<ReactiveList>): Observable<out ReactiveList> {
        val queryChangedObservable = transformQueryEvent(eventStream)
        val flatMap = eventStream.flatMap(::flatMap)

        return Observable.merge(queryChangedObservable, flatMap)
    }

    override fun flatMap(event: ReactiveList): Observable<out ReactiveList> {
        return when (event) {
            is ReactiveList.Lifecycle -> reactOnLifecycle(event.stage)
            is ReactiveList.Reload -> loadData()
            is ReactiveList.SwipeRefresh -> loadData(isSwr = true)
            is ReactiveList.LoadNextPage -> loadData(sh.state.data.nextPage)
            else -> skip()
        }
    }

    fun transformQueryEvent(
            eventStream: Observable<ReactiveList>
    ): Observable<ReactiveList.QueryChangedDebounced> =
            eventStream
                    .filter { it is ReactiveList.QueryChanged }
                    .map { (it as ReactiveList.QueryChanged).query }
                    .distinctUntilChanged()
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .map { ReactiveList.QueryChangedDebounced(it) }


    private fun reactOnLifecycle(stage: LifecycleStage): Observable<out ReactiveList> =
            when (stage) {
                LifecycleStage.CREATE -> loadData()
                else -> skip()
            }

    private fun loadData(page: Int = 0, isSwr: Boolean = false) = createObservable(page)
            .io()
            .handleError()
            .mapToLoadable(ReactiveList.Numbers(isSwr = isSwr))

    private fun createObservable(page: Int = 0) = Observable.timer(2, TimeUnit.SECONDS).map {
        DataList<String>(
                (1..20).map { (it + page * 20).toString() },
                page,
                20
        )
    }
}