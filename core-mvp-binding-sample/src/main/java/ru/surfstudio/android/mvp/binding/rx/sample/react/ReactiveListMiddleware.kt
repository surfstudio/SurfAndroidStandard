package ru.surfstudio.android.mvp.binding.rx.sample.react

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleStage
import ru.surfstudio.android.mvp.binding.rx.sample.react.base.middleware.BaseMiddleware
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.base.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ReactiveListEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.reactor.ReactiveListStateHolder
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class ReactiveListMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val sh: ReactiveListStateHolder
) : BaseMiddleware<ReactiveListEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<ReactiveListEvent>): Observable<out ReactiveListEvent> {
        val queryChangedObservable = transformQueryEvent(eventStream)
        val flatMap = eventStream.flatMap(::flatMap)

        return Observable.merge(queryChangedObservable, flatMap)
    }

    override fun flatMap(event: ReactiveListEvent): Observable<out ReactiveListEvent> {
        return when (event) {
            is ReactiveListEvent.LifecycleChanged -> reactOnLifecycle(event.stage)
            is ReactiveListEvent.Reload -> loadData()
            is ReactiveListEvent.SwipeRefresh -> loadData(isSwr = true)
            is ReactiveListEvent.LoadNextPage -> loadData(sh.state.data.nextPage)
            else -> skip()
        }
    }

    fun transformQueryEvent(
            eventStream: Observable<out ReactiveListEvent>
    ): Observable<ReactiveListEvent.QueryChangedDebounced> =
            eventStream
                    .filter { it is ReactiveListEvent.QueryChanged }
                    .map { (it as ReactiveListEvent.QueryChanged).query }
                    .distinctUntilChanged()
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .map { ReactiveListEvent.QueryChangedDebounced(it) }


    private fun reactOnLifecycle(stage: LifecycleStage): Observable<out ReactiveListEvent> =
            when (stage) {
                LifecycleStage.CREATE -> loadData()
                else -> skip()
            }

    private fun loadData(page: Int = 0, isSwr: Boolean = false) = createObservable(page)
            .io()
            .handleError()
            .mapToLoadable(ReactiveListEvent.LoadNumbers(isSwr = isSwr))

    private fun createObservable(page: Int = 0) = Observable.timer(2, TimeUnit.SECONDS).map {
        DataList<String>(
                (1..20).map { (it + page * 20).toString() },
                page,
                20
        )
    }
}