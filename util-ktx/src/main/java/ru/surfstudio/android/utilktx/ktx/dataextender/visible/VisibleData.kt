package ru.surfstudio.android.utilktx.ktx.dataextender.visible

import ru.surfstudio.android.utilktx.ktx.dataextender.BaseDataExtender

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