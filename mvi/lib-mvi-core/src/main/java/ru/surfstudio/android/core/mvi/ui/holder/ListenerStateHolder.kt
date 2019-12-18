package ru.surfstudio.android.core.mvi.ui.holder

import java.lang.NullPointerException

/**
 * Наследник [ReducerStateHolder], работающий через обычный коллбек.
 */
open class ListenerStateHolder<S>(initialValue: S? = null) :
        ReducerStateHolder<S>() {

    private var stateChangedListener: StateChangedListener<S>? = null

    private var internalState: S? = initialValue

    override var state: S
        get() = internalState ?: throw NullPointerException()
        set(value) {
            val oldValue = internalState
            if (isChanged(oldValue, value))
                stateChangedListener?.invoke(value)
            internalState = value
        }

    /**
     * Функция для проверки на то, изменилась ли модель экрана
     */
    protected open fun isChanged(oldValue: S?, newValue: S): Boolean = newValue == oldValue

    /**
     * Добавление лиснера на изменение состояния экрана
     */
    fun listen(listener: StateChangedListener<S>) {
        this.stateChangedListener = listener
    }
}