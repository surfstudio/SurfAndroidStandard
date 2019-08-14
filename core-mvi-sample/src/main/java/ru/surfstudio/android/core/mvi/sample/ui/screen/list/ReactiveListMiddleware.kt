package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.*
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ReactiveListEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.canGetMore
import ru.surfstudio.android.core.mvi.ui.middleware.builders.LifecycleMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.PaginationMiddleware

@PerScreen
class ReactiveListMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val sh: ReactiveListStateHolder
) : BaseMiddleware<ReactiveListEvent>(baseMiddlewareDependency),
        LifecycleMiddleware<ReactiveListEvent>,
        PaginationMiddleware<ReactiveListEvent> {

    override fun transform(eventStream: Observable<ReactiveListEvent>): Observable<out ReactiveListEvent> {
        return transformations(eventStream) {
            +onCreate().eventMap { loadData() }
            +eventMap<SwipeRefresh> { loadData(isSwr = true) }
            +eventMap<LoadNextPage> { loadData(sh.list.data.nextPage) }
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