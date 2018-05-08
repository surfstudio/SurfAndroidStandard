package ru.surfstudio.android.core.mvp.binding

/**
 * Класс обеспечивающий подписку на изменение данных
 */
open class BindData<T>(value: T) {

    var value: T = value
        private set

    val listeners: MutableMap<Any, MutableSet<((T) -> Unit)>> = mutableMapOf()

    fun observe(source: Any, listener: (T) -> Unit) {
        listeners[source]?.add(listener)
        if (listeners[source] == null) listeners[source] = mutableSetOf(listener)
    }

    fun observeAndInvoke(source: Any, listener: (T) -> Unit) {
        observe(source, listener)
        listener.invoke(value)
    }

    fun setValue(newValue: T, source: Any?) {
        value = newValue
        listeners.filterKeys { it != source }
                .values
                .forEach { it.forEach { it.invoke(value) } }
    }
}

