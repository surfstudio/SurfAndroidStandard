package ru.surfstudio.android.utilktx.ktx.dataextender.blockable

import ru.surfstudio.android.utilktx.ktx.dataextender.BaseDataExtender

/**
 * Если может блокировать своё состояние
 */
interface BlockableDataInterface {
    var isBlocked: Boolean
    fun block()
    fun unblock()
}

class BlockableData<T>(data: T)
    : BaseDataExtender<T>(data), BlockableDataInterface {

    override var isBlocked: Boolean = false

    override fun block() {
        isBlocked = true
    }

    override fun unblock() {
        isBlocked = false
    }
}