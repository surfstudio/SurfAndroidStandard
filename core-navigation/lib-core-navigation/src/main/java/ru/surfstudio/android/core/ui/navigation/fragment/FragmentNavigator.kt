package ru.surfstudio.android.core.ui.navigation.fragment

import android.app.FragmentTransaction.*
import androidx.annotation.IntDef
import ru.surfstudio.android.core.ui.navigation.Navigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute

/**
 * позволяет осуществлять навигацияю между фрагментами
 */
interface FragmentNavigator : Navigator {

    fun add(route: FragmentRoute?, stackable: Boolean, @Transit transition: Int)

    fun replace(route: FragmentRoute?, stackable: Boolean, @Transit transition: Int)

    /**
     * @return возвращает true если фрагмент был удален успешно
     */
    fun remove(route: FragmentRoute?, @Transit transition: Int): Boolean

    /**
     * @return возвращает true если фрагмент успешно отобразился
     */
    fun show(route: FragmentRoute?, @Transit transition: Int): Boolean

    /**
     * @return возвращает true если фрагмент был скрыт успешно
     */
    fun hide(route: FragmentRoute?, @Transit transition: Int): Boolean

    /**
     * @return возвращает true если какой-либо фрагмент верхнего уровня был удален из стека
     */
    fun popBackStack(): Boolean

    /**
     * Очищает стек фрагментов до роута.
     * Пример:
     * Фрагменты А, Б, С, Д добавлены в стек
     * popBackStack(Б, true) очистит стек до Б включительно
     * то есть, останется в стеке только А
     * или
     * popBackStack(Б, false) в стеке останется А и Б,
     * Б не удаляется из стека
     *
     * @param route     очистка до уровня роута
     * @param inclusive удалить стек включая и роут
     * @return возвращает true если фрагмент(ы) был(и) удален(ы) из стека
     */
    fun popBackStack(route: FragmentRoute, inclusive: Boolean): Boolean

    /**
     * Очистка бэкстека
     *
     * @return true если успешно
     */
    fun clearBackStack(): Boolean

    @IntDef(TRANSIT_NONE, TRANSIT_FRAGMENT_OPEN, TRANSIT_FRAGMENT_CLOSE, TRANSIT_FRAGMENT_FADE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Transit
}