package ru.surfstudio.android.utilktx.ktx.dataextender.scrollable

import ru.surfstudio.android.utilktx.ktx.dataextender.BaseDataExtender

/**
 * Если внутри элемента есть другой список
 * и необходимо запоминать позицию внутреннего списка
 */
interface ScrollableDataInterface {
    var scrollPosition: Int
}

class ScrollableData<T>(data: T) : BaseDataExtender<T>(data), ScrollableDataInterface {

    private val START_POSITION = 0

    override var scrollPosition: Int = START_POSITION
}