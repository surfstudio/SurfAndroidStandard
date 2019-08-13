package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.event.ReactiveListEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.reactor.ReactiveListStateHolder
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.event.ReactiveListEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.extension.canGetMore
import ru.surfstudio.android.core.mvi.ui.middleware.builders.LifecycleMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.builders.LoadNextPageMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.PaginationMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.builders.SwrMiddleware

@PerScreen
class ReactiveListMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val sh: ReactiveListStateHolder
) : BaseMiddleware<ReactiveListEvent>(baseMiddlewareDependency),
        SwrMiddleware<ReactiveListEvent>,
        LoadNextPageMiddleware<ReactiveListEvent>,
        LifecycleMiddleware<ReactiveListEvent>,
        PaginationMiddleware<ReactiveListEvent> {

    override fun transform(eventStream: Observable<ReactiveListEvent>): Observable<out ReactiveListEvent> {
        return merge(eventStream) {
            +onCreate().eventMap { loadData() }
            +mapSwr()
            +mapLoadNext()
            +mapPagination(FilterNumbers()) { sh.list.canGetMore() }
            +map<QueryChangedDebounced> { FilterNumbers() }
            +eventMap<Reload> { loadData() }
            +streamMap(::debounceQuery)
        }
    }

    override fun flatMap(event: ReactiveListEvent): Observable<out ReactiveListEvent> =
            skip()

    private fun debounceQuery(
            eventStream: Observable<ReactiveListEvent>
    ): Observable<out ReactiveListEvent> =
            eventStream
                    .filterIsInstance<QueryChanged>()
                    .map { it.query }
                    .distinctUntilChanged()
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .map { QueryChangedDebounced(it) }

    override fun loadDataSwr(): Observable<out ReactiveListEvent> = loadData(isSwr = true)

    override fun loadNextData() = loadData(sh.list.data.nextPage)

    private fun loadData(page: Int = 1, isSwr: Boolean = false) = createObservable(page)
            .io()
            .handleError()
            .mapResponseEvent(LoadList(isSwr = isSwr))

    private fun createObservable(page: Int) = Observable.timer(2, TimeUnit.SECONDS).map {
        DataList(
                (1..20).map { (it + (page - 1) * 20).toString() },
                page,
                20,
                100,
                5
        )
    }
}