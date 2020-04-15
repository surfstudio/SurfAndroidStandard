package ru.surfstudio.android.core.mvi.impls.ui.reactor

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.mvp.binding.rx.request.data.*
import ru.surfstudio.android.core.mvp.binding.rx.request.state.RequestState
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.rx.extension.scheduler.MainThreadImmediateScheduler
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList as OffsetDataList
import ru.surfstudio.android.datalistlimitoffset.ui.PaginationBundle as OffsetPaginationBundle
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList as PageDataList
import ru.surfstudio.android.datalistpagecount.ui.PaginationBundle as PagePaginationBundle

/**
 * Base implementation of [Reactor].
 *
 * Contains many useful methods to handle requests easily with error handling.
 * */
abstract class BaseReactor<E : Event, SH>(
        baseReactorDependency: BaseReactorDependency
) : Reactor<E, SH> {

    protected val errorHandler = baseReactorDependency.errorHandler

    /**
     * Map request with default algorithm.
     * */
    protected fun <T> mapRequestDefault(
            request: Request<T>,
            requestUi: RequestUi<T>,
            isSwr: Boolean = false
    ): RequestUi<T> {
        val newData = mapData(request, requestUi.data)
        val hasData = newData != null
        val newLoading = mapLoading(request, hasData, isSwr)
        val newError = mapError(request, hasData, requestUi.error)
        return RequestUi(newData, newLoading, newError)
    }

    /**
     * Map request with default algorithm, but with advanced `errorHandling` control.
     *
     * If you don't handle error in `customErrorHandler` - error will be sent to `errorHandler`.
     * */
    protected fun <T> mapRequestWithErrorHandlerPriority(
            request: Request<T>,
            requestUi: RequestUi<T>,
            isSwr: Boolean = false,
            customErrorHandler: (Throwable) -> Boolean = { false }
    ): RequestUi<T> {
        val newData = mapData(request, requestUi.data)
        val hasData = newData != null
        val newLoading = mapLoading(request, hasData, isSwr)
        val newError = when {
            request.isError -> request.getError().also {
                val isHandled = customErrorHandler(it)
                if (!isHandled) handleErrorInMainThread(it)
            }
            else -> requestUi.error
        }
        return RequestUi(newData, newLoading, newError)
    }

    /**
     * Map page-count based pagination request.
     * */
    protected fun <T> mapRequestPagePagination(
            request: Request<PageDataList<T>>,
            requestUi: RequestUi<PagePaginationBundle<T>>,
            isSwr: Boolean = false
    ): RequestUi<PagePaginationBundle<T>> {
        val newData = mapPagePaginationBundle(request, requestUi.data)
        val hasData = newData.hasData
        val newLoadState = mapLoading(request, hasData, isSwr)
        val newError = mapError(request, hasData, requestUi.error)
        return RequestUi(newData, newLoadState, newError)
    }

    /**
     * Map limit-offset based pagination request.
     * */
    protected fun <T> mapRequestOffsetPagination(
            request: Request<OffsetDataList<T>>,
            requestUi: RequestUi<OffsetPaginationBundle<T>>,
            isSwr: Boolean = false
    ): RequestUi<OffsetPaginationBundle<T>> {
        val newData = mapOffsetPaginationBundle(request, requestUi.data)
        val hasData = newData.hasData
        val newLoadState = mapLoading(request, hasData, isSwr)
        val newError = mapError(request, hasData, requestUi.error)
        return RequestUi(newData, newLoadState, newError)
    }

    /**
     * Map [request]'s data.
     * */
    protected fun <T> mapData(request: Request<T>, data: T?): T? {
        return request.getDataOrNull() ?: data
    }

    /**
     * Map [request]'s loading-state.
     * */
    protected fun <T> mapLoading(request: Request<T>, hasData: Boolean, isSwr: Boolean = false): Loading {
        return when {
            isSwr -> SwipeRefreshLoading(request.isLoading)
            hasData -> TransparentLoading(request.isLoading)
            else -> MainLoading(request.isLoading)
        }
    }

    /**
     * Map [request]'s error.
     * */
    protected fun <T> mapError(
            request: Request<T>,
            hasData: Boolean,
            lastError: Throwable?,
            sideEffects: (Throwable) -> Unit = {}
    ): Throwable? {

        val mappedError = when {
            hasData -> null
            request.isError -> request.getError().also(sideEffects)
            lastError != null -> lastError
            else -> null
        }

        if (request.isError && mappedError != null) {
            handleErrorInMainThread(mappedError)
        }

        return mappedError
    }

    /**
     * Map page-count based pagination bundle.
     * */
    protected fun <T> mapPagePaginationBundle(
            request: Request<PageDataList<T>>,
            paginationBundle: PagePaginationBundle<T>?
    ): PagePaginationBundle<T> {
        val hasData = paginationBundle?.hasData ?: false
        val newDataList = mapPageDataList(request, paginationBundle?.data, hasData)
        val canGetMore = newDataList?.canGetMore() == true
        val newState = when (request) {
            is Request.Loading -> null
            is Request.Success -> if (canGetMore) PaginationState.READY else PaginationState.COMPLETE
            is Request.Error -> PaginationState.ERROR
        }
        return PagePaginationBundle(newDataList, newState)
    }

    /**
     * Map limit-offset based pagination bundle.
     * */
    protected fun <T> mapOffsetPaginationBundle(
            request: Request<OffsetDataList<T>>,
            paginationBundle: OffsetPaginationBundle<T>?
    ): OffsetPaginationBundle<T> {
        val hasData = paginationBundle?.hasData ?: false
        val newDataList = mapOffsetDataList(request, paginationBundle?.data, hasData)
        val canGetMore = newDataList?.canGetMore() == true
        val newState = when (request) {
            is Request.Loading -> null
            is Request.Success -> if (canGetMore) PaginationState.READY else PaginationState.COMPLETE
            is Request.Error -> PaginationState.ERROR
        }
        return OffsetPaginationBundle(newDataList, newState)
    }

    /**
     * Map page-count based pagination data.
     * */
    protected fun <T> mapPageDataList(
            request: Request<PageDataList<T>>,
            currentData: PageDataList<T>?,
            hasData: Boolean
    ): PageDataList<T>? {
        return when {
            request.isSuccess -> {
                val newData = request.getDataOrNull()
                val isReload = newData?.numPages == 1
                when {
                    hasData && !isReload -> currentData?.merge(newData)
                    else -> newData
                }
            }
            else -> currentData
        }
    }

    /**
     * Map limit-offset based pagination data.
     * */
    protected fun <T> mapOffsetDataList(
            request: Request<OffsetDataList<T>>,
            currentData: OffsetDataList<T>?,
            hasData: Boolean
    ): OffsetDataList<T>? {
        return when {
            request.isSuccess -> {
                val newData = request.getDataOrNull()
                val isReload = newData?.offset == 0
                when {
                    hasData && !isReload -> currentData?.merge(newData)
                    else -> newData
                }
            }
            else -> currentData
        }
    }

    /**
     * Изменяет текущий [RequestState] в зависимости от полученного [RequestEvent]
     * Если ранее в RequestState были данные, то ошибка в него не придет.
     *
     * @param event полученный [RequestEvent]
     * @param isSwr true если запрос выполняется по свайп рефрешу
     * @param errorSideEffects дополнительные действия, выполняемые при получении ошибки.
     *                          Переопределяются, когда нам нужно, например, показать снек
     */
    fun <T> RequestState<T>.modifyDefault(
            event: RequestEvent<T>,
            isSwr: Boolean = false,
            errorSideEffects: (Throwable) -> Unit = {}
    ) {
        modify {
            val request = event.request
            val newData = mapData(request, data)
            val hasData = newData != null

            copy(
                    data = newData,
                    load = mapLoading(request, hasData, isSwr),
                    error = mapError(request, hasData, error, errorSideEffects)
            )
        }
    }

    /**
     * Modify `RequestState` with default algorithm, but with advanced `errorHandling` control.
     *
     * If you don't handle error in `customErrorHandler` - error will be sent to `errorHandler`.
     * */
    protected fun <T> RequestState<T>.modifyWithErrorHandlerPriority(
            event: RequestEvent<T>,
            isSwr: Boolean = false,
            customErrorHandler: (Throwable) -> Boolean = { false }
    ) {
        modify {
            mapRequestWithErrorHandlerPriority(event.request, this, isSwr, customErrorHandler)
        }
    }

    /**
     * Изменяет текущий [RequestUi], типизированный по [PagePaginationBundle] в соответствии с пришедшим [event].
     * Если данные уже были получены, состояние ошибки не будет показано.
     *
     * @param event событие с новыми данными для изменения текущего [RequestUi]
     * @param isSwr true если событие является SwipeRefresh, иначе false
     */
    fun <T> RequestState<PagePaginationBundle<T>>.modifyPagePaginationRequest(
            event: RequestEvent<PageDataList<T>>,
            isSwr: Boolean
    ) {
        modify { mapRequestPagePagination(event.request, this, isSwr) }
    }

    /**
     * Изменяет текущий [RequestUi], типизированный по [OffsetPaginationBundle] в соответствии с пришедшим [event].
     * Если данные уже были получены, состояние ошибки не будет показано.
     *
     * @param event событие с новыми данными для изменения текущего [RequestUi]
     * @param isSwr true если событие является SwipeRefresh, иначе false
     */
    fun <T> RequestState<OffsetPaginationBundle<T>>.modifyOffsetPaginationRequest(
            event: RequestEvent<OffsetDataList<T>>,
            isSwr: Boolean
    ) {
        modify { mapRequestOffsetPagination(event.request, this, isSwr) }
    }

    /**
     * Executes [action] when request is succeed.
     * */
    protected fun <T> RequestEvent<T>.doOnSuccess(action: (T) -> Unit) {
        request.getDataOrNull()?.let(action)
    }

    /**
     * Executes [action] when request is loading.
     * */
    protected fun RequestEvent<*>.doOnLoading(action: () -> Unit) {
        if (request.isLoading) action()
    }

    /**
     * Executes [action] when request is failed.
     * */
    protected fun RequestEvent<*>.doOnError(action: (Throwable) -> Unit) {
        request.getErrorOrNull()?.let(action)
    }

    /**
     * Post error handling of [error] to main thread.
     * */
    protected fun handleErrorInMainThread(error: Throwable) {
        MainThreadImmediateScheduler.scheduleDirect { errorHandler.handleError(error) }
    }
}