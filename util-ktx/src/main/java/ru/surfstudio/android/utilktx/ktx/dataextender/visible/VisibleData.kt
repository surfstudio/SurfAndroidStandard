package com.company.uidata.dataextender.visible

import com.company.uidata.dataextender.BaseDataExtender

/**
 * Если имеет возможность скрываться
 */
interface VisibleDataInterface {
    var isVisible: Boolean
    fun visible()
    fun invisible()
}

class VisibleData<T>(data: T)
    : BaseDataExtender<T>(data), VisibleDataInterface {

    override var isVisible: Boolean = true

    override fun visible() {
        isVisible = true
    }

    override fun invisible() {
        isVisible = false
    }
}