package ru.surfstudio.android.core.mvp.binding

/**
 * Класс обеспечивающий подписку на изменение данных.
 * Реализует концепцию источников данных: "События изменения данных принимают все, кроме эммитящего источника"
 * Это исключает прослушивания источником своих же событий изменения данных и зацикливания.
 */
open class BindData<T>(value: T) {

    var value: T = value
        private set

    private val listeners: MutableMap<Any, MutableSet<((T) -> Unit)>> = mutableMapOf()

    /**
     * Подписка на изменеие значения
     * @param source источник получения данных
     */
    fun observe(source: Any, listener: (T) -> Unit) {
        listeners[source]?.add(listener)
        if (listeners[source] == null) listeners[source] = mutableSetOf(listener)
    }

    /**
     * Подписывает на изменение значения и вызывает подписку для с текущим значением
     */
    fun observeAndApply(source: Any, listener: (T) -> Unit) {
        observe(source, listener)
        listener.invoke(value)
    }

    /**
     * Устанавливает значение и оповещает всех подписчиков, кроме указанного источника
     * @param source источник изменения значения
     * @param newValue новое значение переменной
     * @param eagerNotify оповещать подписчиков, даже если значение не изменилось
     */
    fun setValue(source: Any, newValue: T, eagerNotify: Boolean = false) {
        val previousValue  = value
        value = newValue
        if (value == previousValue && !eagerNotify) return

        listeners.filterKeys { it != source }
                .values
                .forEach { it.forEach { it.invoke(value) } }
    }

    /**
     * Отписывает источник от всех событий
     */
    fun unObserveSource(source: Any) {
        listeners.remove(source)
    }
}

