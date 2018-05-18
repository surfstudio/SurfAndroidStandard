package ru.surfstudio.android.utilktx.ktx.datawrapper.scrollable

import ru.surfstudio.android.utilktx.ktx.datawrapper.BaseDataWrapper

/**
 * Интерфейс сущности, которая имеет внутри элемента другой список
 * и необходимо запоминать позицию внутреннего списка
 */
interface ScrollableDataInterface {
    var scrollPosition: Int
}

class ScrollableData<T>(data: T) : BaseDataWrapper<T>(data), ScrollableDataInterface {

    private val START_POSITION = 0

    override var scrollPosition: Int = START_POSITION
}