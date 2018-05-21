package ru.surfstudio.android.utilktx.ktx.datawrapper.loadable

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая может иметь состояние загрузки
 */
interface LoadableDataInterface {
    var loadStateData: LoadStatus
    fun setNormal()
    fun isNormal(): Boolean
    fun setLoading()
    fun isLoading(): Boolean
    fun setErrorLoading()
    fun isErrorLoading(): Boolean
}

class LoadableData<T>(override var data: T)
    : DataWrapperInterface<T>, LoadableDataInterface {

    override var loadStateData: LoadStatus = LoadStatus.NORMAL

    override fun setNormal() {
        this.loadStateData = LoadStatus.NORMAL
    }

    override fun isNormal(): Boolean =
            loadStateData == LoadStatus.NORMAL

    override fun setLoading() {
        this.loadStateData = LoadStatus.LOADING
    }

    override fun isLoading(): Boolean =
            loadStateData == LoadStatus.LOADING

    override fun setErrorLoading() {
        this.loadStateData = LoadStatus.ERROR
    }

    override fun isErrorLoading(): Boolean =
            loadStateData == LoadStatus.ERROR

}

enum class LoadStatus {
    NORMAL, //обычное состояние
    LOADING, //загружается
    ERROR //произошла ошибка
}