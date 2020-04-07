package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.util

import ru.surfstudio.android.core.mvi.ui.reducer.RequestDataMapper
import ru.surfstudio.android.core.mvi.ui.reducer.RequestErrorHandler
import ru.surfstudio.android.core.mvi.ui.reducer.RequestLoadingMapper
import ru.surfstudio.android.core.mvp.binding.rx.request.data.MainLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SimpleLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SwipeRefreshLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.TransparentLoading
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.pagination.PaginationState

/**
 * Singleton-фабрика мапперов запросов.
 * */
object RequestMappers {

    /**
     * Мапперы данных запроса.
     * */
    val data = DataMappers

    /**
     * Мапперы состояния загрузки запроса.
     * */
    val loading = LoadingMappers

    /**
     * Обработчики ошибок запроса.
     * */
    val error = ErrorHandlers

    /**
     * Мапперы данных запроса.
     * */
    object DataMappers {

        /**
         * Маппер данных запроса, который каждый новый запрос "очищает" данные в стейте.
         *
         * @return только что полученные данные из запроса.
         * */
        fun <T> single(): RequestDataMapper<T, T, T> =
                { request, _ -> request.dataOrNull }

        /**
         * Стандартный маппер данных запроса.
         *
         * @return только что полученные данные из запроса, либо данные из стейта.
         * */
        fun <T> default(): RequestDataMapper<T, T, T> =
                { request, data -> request.dataOrNull ?: data }

        /**
         * Маппер данных пагинируемого запроса.
         *
         * @return только что полученные данные из запроса (опционально смерженные с данными из стейта),
         *         либо данные из стейта.
         * */
        fun <T> pagination(): RequestDataMapper<DataList<T>, PaginationBundle<T>, PaginationBundle<T>> =
                { request, data ->
                    val currentDataList = data?.list
                    val newDataList = request.dataOrNull

                    val hasCurrentData = currentDataList != null
                    val hasNewData = newDataList != null

                    val isNewDataListHasOffset = (newDataList?.offset ?: 0) != 0
                    val canMergeLists = hasNewData && isNewDataListHasOffset && hasCurrentData

                    val mappedDataList = when {
                        canMergeLists -> currentDataList?.merge(newDataList)
                        hasNewData -> newDataList
                        else -> currentDataList
                    }

                    val hasMoreData = mappedDataList?.canGetMore() ?: false
                    val mappedPaginationState = when {
                        request.isSuccess && !hasMoreData -> PaginationState.COMPLETE
                        request.isSuccess && hasMoreData -> PaginationState.READY
                        request.isError -> PaginationState.ERROR
                        else -> null
                    }

                    PaginationBundle(mappedDataList, mappedPaginationState)
                }
    }

    /**
     * Мапперы состояния загрузки запроса.
     * */
    object LoadingMappers {

        fun <T1, T2> simple(): RequestLoadingMapper<T1, T2> =
                { request, _ -> SimpleLoading(request.isLoading) }

        /**
         * Стандартный маппер состояния загрузки запроса.
         *
         * @return актуальное состояние загрузки.
         * */
        fun <T1, T2> default(isSwr: Boolean = false): RequestLoadingMapper<T1, T2> =
                { request, data ->
                    val hasData = data != null
                    when {
                        isSwr -> SwipeRefreshLoading(request.isLoading)
                        hasData -> TransparentLoading(request.isLoading)
                        else -> MainLoading(request.isLoading)
                    }
                }

        /**
         * Маппер состояния загрузки пагинируемого запроса.
         *
         * @return актуальное состояние загрузки.
         * */
        fun <T> pagination(isSwr: Boolean = false): RequestLoadingMapper<DataList<T>, PaginationBundle<T>> =
                { request, data ->
                    val hasData = data?.hasData ?: false
                    when {
                        isSwr -> SwipeRefreshLoading(request.isLoading)
                        hasData -> TransparentLoading(request.isLoading)
                        else -> MainLoading(request.isLoading)
                    }
                }
    }

    /**
     * Обработчики ошибок запроса.
     * */
    object ErrorHandlers {

        /**
         * Обработчик ошибок, который каждую из них отправляет в [ErrorHandler].
         *
         * @return Была ли обработана ошибка?
         * */
        fun <T> forced(errorHandler: ErrorHandler): RequestErrorHandler<T> =
                { error, _, _ ->
                    error?.let(errorHandler::handleError)
                    true
                }
    }
}