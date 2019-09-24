package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.*
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ComplexListEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.canGetMore
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.ui.middleware.builders.LifecycleMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.pagination.PaginationMiddleware
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList

/**
 * Middleware экрана [ComplexListActivityView]
 */
@PerScreen
class ComplexListMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val sh: ComplexListStateHolder
) : BaseMiddleware<ComplexListEvent>(baseMiddlewareDependency),
        LifecycleMiddleware<ComplexListEvent>,
        PaginationMiddleware<ComplexListEvent> {

    override fun transform(eventStream: Observable<ComplexListEvent>): Observable<out ComplexListEvent> {
        return transformations(eventStream) {
            +onCreate().eventMap { loadData() }
            +eventMap<SwipeRefresh> { loadData(isSwr = true) }
            +eventMap<LoadNextPage> { loadData(sh.list.data.nextPage) }
            +mapPagination(FilterNumbers()) { sh.list.canGetMore() }
            +map<QueryChangedDebounced> { FilterNumbers() }
            +eventMap<Reload> { loadData() }
            +streamMap<ComplexListEvent>(::debounceQuery)
        }
    }

    private fun debounceQuery(
            eventStream: Observable<ComplexListEvent>
    ): Observable<out ComplexListEvent> =
            eventStream
                    .filterIsInstance<QueryChanged>()
                    .map { it.query }
                    .distinctUntilChanged()
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .map { QueryChangedDebounced(it) }

    private fun loadData(page: Int = 1, isSwr: Boolean = false) = createObservable(page)
            .io()
            .handleError()
            .asRequestEvent(LoadList(isSwr = isSwr))

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