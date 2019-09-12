package ru.surfstudio.android.mvp.widget.event

/**
 * Источник сообщения о смене жизненного цикла view
 *
 * @property PARENT     событие пришло из родителя
 * @property DELEGATE   событие пришло из делегата
 * @property LIFECYCLE_MANAGER событие пришло из LifecycleManager
 */
enum class StageSource {
    PARENT,
    DELEGATE,
    LIFECYCLE_MANAGER
}