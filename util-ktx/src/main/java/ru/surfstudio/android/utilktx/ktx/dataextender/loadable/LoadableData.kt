package ru.surfstudio.android.utilktx.ktx.dataextender.loadable

import ru.surfstudio.android.utilktx.ktx.dataextender.BaseDataExtender

/**
 * Если может иметь состояние загрузки
 */
interface LoadableDataInterface {
    var loadStateData: LoadStateData
}

class LoadableData<T>(data: T)
    : BaseDataExtender<T>(data), LoadableDataInterface {

    override var loadStateData: LoadStateData = LoadStateData.NONE
}

enum class LoadStateData {
    NONE, //обычное состояние
    LOADING, //загружается
    ERROR //произошла ошибка
}