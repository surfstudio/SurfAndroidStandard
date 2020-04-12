package ru.surfstudio.android.core.mvi.sample.ui.screen.list

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.canGetMore
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.pagination.PaginationMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ComplexListEvent.*
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.LifecycleMiddleware
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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
            addAll(
                    onCreate() eventMap { loadData() },
                    mapPagination(FilterNumbers()) { sh.list.canGetMore() },
                    eventMap<SwipeRefresh> { loadData(isSwr = true) },
                    SwipeRefresh::class eventMapTo { loadData(isSwr = true) },
                    LoadNextPage::class eventMapTo { loadData(sh.list.data?.nextPage ?: 0) },
                    Reload::class eventMapTo { loadData() },
                    QueryChanged::class streamMapTo ::debounceQuery,
                    QueryChangedDebounced::class mapTo { FilterNumbers() }
            )
        }
    }


    private fun debounceQuery(
            eventStream: Observable<QueryChanged>
    ): Observable<ComplexListEvent> =
            eventStream
                    .map { it.query }
                    .distinctUntilChanged()
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .map { QueryChangedDebounced(it) }

    private fun loadData(page: Int = 1, isSwr: Boolean = false) = createObservable(page)
            .io()
            .handleError()
            .asRequestEvent { LoadList(request = it, isSwr = isSwr) }

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