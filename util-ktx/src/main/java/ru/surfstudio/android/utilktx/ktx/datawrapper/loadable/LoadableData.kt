package ru.surfstudio.android.utilktx.ktx.datawrapper.loadable

import ru.surfstudio.android.utilktx.ktx.datawrapper.BaseDataWrapper

/**
 * Интерфейс сущности, которая может иметь состояние загрузки
 */
interface LoadableDataInterface {
    var loadStateData: LoadStateData
}

class LoadableData<T>(data: T)
    : BaseDataWrapper<T>(data), LoadableDataInterface {

    override var loadStateData: LoadStateData = LoadStateData.NORMAL

    fun setNormal() {
        this.loadStateData = LoadStateData.NORMAL
    }

    fun isNormal(): Boolean =
            loadStateData == LoadStateData.NORMAL

    fun setLoading() {
        this.loadStateData = LoadStateData.LOADING
    }

    fun isLoading(): Boolean =
            loadStateData == LoadStateData.LOADING

    fun setErrorLoading() {
        this.loadStateData = LoadStateData.ERROR
    }

    fun isErrorLoading(): Boolean =
            loadStateData == LoadStateData.ERROR

}

enum class LoadStateData {
    NORMAL, //обычное состояние
    LOADING, //загружается
    ERROR //произошла ошибка
}