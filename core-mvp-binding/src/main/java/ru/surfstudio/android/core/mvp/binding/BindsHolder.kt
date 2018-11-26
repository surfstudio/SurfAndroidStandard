package ru.surfstudio.android.core.mvp.binding

/**
 * Вспомогательный класс для управления подписками/слушателями
 */
class BindsHolder(private val source: Any) {

    val binds: MutableSet<IBindData<*>> = mutableSetOf()

    fun <T> observe(bindData: IBindData<T>, listener: (T) -> Unit) {
        bindData.observe(source, listener)
        binds.add(bindData)
    }

    fun <T> observeAndApply(bindData: IBindData<T>, listener: (T) -> Unit) {
        bindData.observeAndApply(source, listener)
        binds.add(bindData)
    }

    fun unObserve() {
        binds.forEach { it.unObserveSource(source) }
    }
}