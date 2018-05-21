package ru.surfstudio.android.utilktx.ktx.datawrapper.loadable

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая может иметь состояние загрузки
 */
interface LoadableDataInterface {
    var loadStateData: LoadStatus
    fun setNormal() {
        this.loadStateData = LoadStatus.NORMAL
    }

    fun isNormal(): Boolean =
            loadStateData == LoadStatus.NORMAL

    fun setLoading() {
        this.loadStateData = LoadStatus.LOADING
    }

    fun isLoading(): Boolean =
            loadStateData == LoadStatus.LOADING

    fun setErrorLoading() {
        this.loadStateData = LoadStatus.ERROR
    }

    fun isErrorLoading(): Boolean =
            loadStateData == LoadStatus.ERROR
}

class LoadableData<T>(override var data: T)
    : DataWrapperInterface<T>, LoadableDataInterface {

    override var loadStateData: LoadStatus = LoadStatus.NORMAL
}

enum class LoadStatus {
    NORMAL, //обычное состояние
    LOADING, //загружается
    ERROR //произошла ошибка
}