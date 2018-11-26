package ru.surfstudio.android.core.mvp.binding

/**
 * Класс обеспечивающий подписку на изменение данных.
 * Реализует концепцию источников данных: "События изменения данных принимают все, кроме эммитящего источника"
 * Это исключает прослушивания источником своих же событий изменения данных и зацикливания.
 */
open class BindData<T>(value: T) : IBindData<T> {

    override val value: T
        get() = innerValue

    private var innerValue: T = value

    private val listeners: MutableMap<Any, MutableSet<((T) -> Unit)>> = mutableMapOf()

    /**
     * Подписка на изменеие значения
     * @param source источник получения данных
     */
    override fun observe(source: Any, listener: (T) -> Unit) {
        listeners[source]?.add(listener)
        if (listeners[source] == null) listeners[source] = mutableSetOf(listener)
    }

    /**
     * Подписывает на изменение значения и вызывает подписку для с текущим значением
     */
    override fun observeAndApply(source: Any, listener: (T) -> Unit) {
        observe(source, listener)
        listener.invoke(innerValue)
    }

    /**
     * Устанавливает значение и оповещает всех подписчиков, кроме указанного источника
     * @param source источник изменения значения
     * @param newValue новое значение переменной
     * @param eagerNotify оповещать подписчиков, даже если значение не изменилось
     */
    override fun setValue(source: Any, newValue: T, eagerNotify: Boolean) {
        val previousValue = innerValue
        innerValue = newValue
        if (innerValue == previousValue && !eagerNotify) return

        listeners.filterKeys { it != source }
                .values
                .forEach { it.forEach { it.invoke(innerValue) } }
    }

    /**
     * Отписывает источник от всех событий
     */
    override fun unObserveSource(source: Any) {
        listeners.remove(source)
    }
}
