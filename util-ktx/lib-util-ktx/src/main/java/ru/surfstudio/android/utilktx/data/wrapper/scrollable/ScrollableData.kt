package ru.surfstudio.android.utilktx.data.wrapper.scrollable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

private const val START_POSITION = 0

/**
 * Интерфейс сущности, которая имеет внутри элемента другой список
 * и необходимо запоминать позицию внутреннего списка
 */
interface ScrollableDataInterface {
    var scrollPosition: Int
}

data class ScrollableData<T>(override var data: T,
                             override var scrollPosition: Int = START_POSITION)
    : DataWrapperInterface<T>, ScrollableDataInterface