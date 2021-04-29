package ru.surfstudio.android.core.mvp.binding.test.navigation.base

import androidx.annotation.CallSuper

/**
 * Базовый класс для тестовых имплементаций навигаторов
 */
abstract class BaseTestNavigator<T : TestNavigationEvent> {

    val events: List<T> get() = mutableEvents

    protected val mutableEvents: MutableList<T> = mutableListOf()

    @CallSuper
    open fun reset() {
        mutableEvents.clear()
    }
}