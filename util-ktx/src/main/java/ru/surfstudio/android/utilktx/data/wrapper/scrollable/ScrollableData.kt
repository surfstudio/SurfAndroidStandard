package ru.surfstudio.android.utilktx.data.wrapper.scrollable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая имеет внутри элемента другой список
 * и необходимо запоминать позицию внутреннего списка
 */
interface ScrollableDataInterface {
    var scrollPosition: Int
}

class ScrollableData<T>(override var data: T) :
        DataWrapperInterface<T>, ScrollableDataInterface {

    private val START_POSITION = 0

    override var scrollPosition: Int = START_POSITION
}