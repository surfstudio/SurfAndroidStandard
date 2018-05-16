package com.company.uidata.dataextender.scrollable

import com.company.uidata.dataextender.BaseDataExtender

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