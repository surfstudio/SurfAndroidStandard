package ru.surfstudio.android.utilktx.data.wrapper.loadable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая может иметь состояние загрузки
 */
interface LoadableDataInterface {

    var loadStatus: LoadStatus

    fun setNormal() {
        this.loadStatus = LoadStatus.NORMAL
    }

    fun isNormal(): Boolean =
            loadStatus == LoadStatus.NORMAL

    fun setLoading() {
        this.loadStatus = LoadStatus.LOADING
    }

    fun isLoading(): Boolean =
            loadStatus == LoadStatus.LOADING

    fun setErrorLoading() {
        this.loadStatus = LoadStatus.ERROR
    }

    fun isErrorLoading(): Boolean =
            loadStatus == LoadStatus.ERROR
}

class LoadableData<T>(override var data: T)
    : DataWrapperInterface<T>, LoadableDataInterface {

    override var loadStatus: LoadStatus = LoadStatus.NORMAL
}

enum class LoadStatus {
    NORMAL, //обычное состояние
    LOADING, //загружается
    ERROR //произошла ошибка
}