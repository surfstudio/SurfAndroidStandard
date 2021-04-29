package ru.surfstudio.android.core.ui.navigation.activity.navigator

import ru.surfstudio.android.core.ui.navigation.Navigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * глобальный навигатор для перехода по экранам не имея доступ
 * к контексту активити (из слоя Interactor)
 */
interface GlobalNavigator : Navigator {

    /**
     * Запуск активити.
     *
     * @param route роутер
     * @return true если активити успешно запущен, иначе false
     */
    fun start(route: ActivityRoute): Boolean
}