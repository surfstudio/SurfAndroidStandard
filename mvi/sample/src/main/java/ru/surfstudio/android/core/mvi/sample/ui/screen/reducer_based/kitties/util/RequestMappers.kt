package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.util

import ru.surfstudio.android.core.mvi.sample.ui.data.PaginationBundle
import ru.surfstudio.android.core.mvi.ui.mapper.RequestDataMapper
import ru.surfstudio.android.core.mvi.ui.mapper.RequestErrorHandler
import ru.surfstudio.android.core.mvi.ui.mapper.RequestLoadingMapper
import ru.surfstudio.android.core.mvp.binding.rx.request.data.*
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import ru.surfstudio.android.pagination.PaginationState
import ru.surfstudio.android.rx.extension.scheduler.MainThreadImmediateScheduler

/**
 * Singleton-фабрика мапперов запросов.
 *
 * Используется для хранения переиспользуемых мапперов внутри проекта.
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
         * ## Маппер данных одиночного запроса.
         *
         * Маппер каждый новый запрос "очищает" данные в стейте.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * Сделать одиночный запрос и узнать результат его выполнения,
         * при этом нам не важно предыдущее его состояние.
         *
         * @return только что полученные данные из запроса.
         * */
        fun <T> single(): RequestDataMapper<T, T, T> =
                { request, _ -> request.getDataOrNull() }

        /**
         * ## Маппер данных стандартного запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * При первом запросе получить данные и сохранить их в стейт;
         * * При последующих запросах, если они удачные - обновить данные в стейте.
         *
         * @return только что полученные данные из запроса, либо данные из стейта.
         * */
        fun <T> default(): RequestDataMapper<T, T, T> =
                { request, data -> request.getDataOrNull() ?: data }

        /**
         * ## Маппер данных пагинируемого запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * При первом запросе получить пагинируемые данные и
         * сохранить их в стейт с преобразованием в `PaginationBundle`;
         * * При последующих запросах, если они удачные - дополнить/обновить данные в стейте;
         *
         * @return только что полученные данные из запроса (опционально смерженные с данными из стейта),
         *         либо данные из стейта.
         * */
        fun <T> pagination(): RequestDataMapper<DataList<T>, PaginationBundle<T>, PaginationBundle<T>> =
                { request, paginationBundle ->
                    val currentDataList = paginationBundle?.data
                    val newDataList = request.getDataOrNull()

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

        /**
         * ## Маппер состояния загрузки простого запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * Получить состояние загрузки запроса в простом формате (загружается/не загружается).
         * * На экране осуществляется несколько запросов, на основании состояния загрузки которых
         * вычисляется все состояние экрана.
         *
         * @return актуальное, простое, состояние загрузки.
         * */
        fun <T1, T2> simple(): RequestLoadingMapper<T1, T2> =
                { request, _ -> SimpleLoading(request.isLoading) }

        /**
         * ## Маппер состояния загрузки стандартного запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * На основании одного запроса - вычислить состояние всего экрана.
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
         * ## Маппер состояния загрузки пагинируемого запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * На основании одного пагириуемого запроса - вычислить состояние всего экрана.
         *
         * @return актуальное состояние загрузки.
         * */
        fun <T> pagination(isSwr: Boolean = false): RequestLoadingMapper<DataList<T>, PaginationBundle<T>> =
                { request, paginationBundle ->
                    val hasData = paginationBundle?.hasData ?: false
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
         * ## Обработчик ошибок. Форсированный.
         *
         * Каждую из ошибок отправляет в [ErrorHandler] и завершает обработку ошибок для запроса.
         *
         * Следует использовать этот маппер в тех случая, когда нам необходимо:
         *
         * * Обработать все возникающие ошибки в [ErrorHandler]'е (например,
         * для одиночного запроса, у которого нету UI репрезентации).
         *
         * @return Была ли обработана ошибка?
         * */
        fun <T> forced(errorHandler: ErrorHandler): RequestErrorHandler<T> =
                { error, _, _ ->
                    error?.let {
                        MainThreadImmediateScheduler.scheduleDirect {
                            errorHandler.handleError(it)
                        }
                    }
                    true
                }

        /**
         * ## Обработчик ошибок. Основанный на состоянии загрузки.
         *
         * Отправляет ошибки в [ErrorHandler] только в том случае,
         * когда на UI не отображается состояние ошибки этого запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * Обработать ошибки запроса, которые не отображены на UI
         * (например, для обычного запроса, который на экране один,
         * или для запроса, который управляет всем состоянием экрана).
         *
         * */
        fun <T> loadingBased(errorHandler: ErrorHandler) : RequestErrorHandler<T> =
                { error, _, loading ->
                    // TODO replace with real project-level error state
                    val projectLevelLoadingErrorState = object : Loading {
                        override val isLoading = false
                    }

                    var isHandled = false
                    val isUiErrorState = loading == projectLevelLoadingErrorState

                    if (!isUiErrorState) {
                        isHandled = true
                        MainThreadImmediateScheduler.scheduleDirect {
                            errorHandler.handleError(error)
                        }
                    }

                    isHandled
                }
    }
}