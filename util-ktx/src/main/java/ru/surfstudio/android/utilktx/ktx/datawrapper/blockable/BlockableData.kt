package ru.surfstudio.android.utilktx.ktx.datawrapper.blockable

import ru.surfstudio.android.utilktx.ktx.datawrapper.BaseDataWrapper

/**
 * Интерфейс сущности, если может блокировать своё состояние
 */
interface BlockableDataInterface {
    var isBlocked: Boolean
    fun block()
    fun unblock()
}

class BlockableData<T>(data: T)
    : BaseDataWrapper<T>(data), BlockableDataInterface {

    override var isBlocked: Boolean = false

    override fun block() {
        isBlocked = true
    }

    override fun unblock() {
        isBlocked = false
    }
}