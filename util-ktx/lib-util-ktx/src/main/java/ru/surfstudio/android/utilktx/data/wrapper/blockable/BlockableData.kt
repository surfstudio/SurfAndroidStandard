package ru.surfstudio.android.utilktx.data.wrapper.blockable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface
import java.io.Serializable

/**
 * Интерфейс сущности, если может блокировать своё состояние
 */
interface BlockableDataInterface {

    var isBlocked: Boolean

    fun block() {
        isBlocked = true
    }

    fun unblock() {
        isBlocked = false
    }

    fun toggleBlocked() {
        isBlocked = !isBlocked
    }
}

data class BlockableData<T>(override var data: T,
                            override var isBlocked: Boolean = false)
    : DataWrapperInterface<T>, BlockableDataInterface, Serializable