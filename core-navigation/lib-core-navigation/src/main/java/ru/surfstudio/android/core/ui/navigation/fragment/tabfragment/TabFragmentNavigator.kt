package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment

import io.reactivex.Observable
import ru.surfstudio.android.core.ui.event.back.OnBackPressedEvent
import ru.surfstudio.android.core.ui.navigation.Navigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.RootFragmentRoute

/**
 * Навигатор для фрагментов в табах
 */
interface TabFragmentNavigator : Navigator {

    val backPressedEventObservable: Observable<OnBackPressedEvent>

    val activeTabReOpenObservable: Observable<Unit>

    /**
     * Показ фрагмента
     */
    fun open(route: FragmentRoute)

    /**
     * Переход к фрагменту на конкретном табе
     * @param clearStack флаг очистки стека под открывающимся фрагментом
     */
    fun <T> showAtTab(tabRoute: T, fragmentRoute: FragmentRoute, clearStack: Boolean = false)
        where T : FragmentRoute,
              T : RootFragmentRoute

    /**
     * Замена текущего фрагмента новым c заменой в стеке
     */
    fun replace(fragmentRoute: FragmentRoute)

    /**
     * Чистит стек выбранных табов, и показывает активный
     */
    fun <T> clearTabs(vararg routes: T) where T : FragmentRoute, T : RootFragmentRoute

    /**
     * Чистит стек активного таба
     */
    fun clearStack()

    /**
     * Очистка активного стека до определенного фрагмента по его роуту
     */
    fun clearStackTo(route: FragmentRoute)
}