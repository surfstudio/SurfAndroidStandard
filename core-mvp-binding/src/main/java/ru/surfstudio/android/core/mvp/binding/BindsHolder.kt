package ru.surfstudio.android.core.mvp.binding

/**
 * Вспомогательный класс для управления подписками/слушателями
 */
class BindsHolder(private val source: Any) {

    val binds: MutableSet<BindData<*>> = mutableSetOf()

    fun <T>observe(bindData: BindData<T>, listener: (T)-> Unit) {
        bindData.observe(source, listener)
        binds.add(bindData)
    }

    fun <T> observeAndApply(bindData: BindData<T>, listener: (T) -> Unit) {
        bindData.observeAndApply(source, listener)
        binds.add(bindData)
    }

    fun unObserve() {
        binds.forEach { it.unObserveSource(source) }
    }
}